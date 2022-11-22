package com.example.models;


import com.example.Database.AccountDatabase;
import com.example.services.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;


import java.util.logging.Logger;


public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private static final Logger LOGGER = Logger.getLogger(BankService.class.getName());
    // Unary --------------------------------------------------------------------------------------
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        int accountNumber = request.getAccountNumber();

        Balance balance = Balance.newBuilder()
                .setAmount(AccountDatabase.getBalance(accountNumber))
                .build();

        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    // Server stream ---------------------------------------------------------------------------------


    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Banknote> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount(); // 10, 10, 30 ...
        int balance = AccountDatabase.getBalance(accountNumber);

        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("Not enough money in your account!");
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        // all the validations are passed successfully
        for (int i = 0; i < (amount/10); i++) {
            Banknote banknote = Banknote.newBuilder()
                    .setValue(10)
                    .build();
            LOGGER.info("10 £");

            responseObserver.onNext(banknote);
            AccountDatabase.deductBalance(accountNumber, 10);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}
