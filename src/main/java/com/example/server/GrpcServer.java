
package com.example.server;


import com.example.models.BankService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {

    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(8089)
                .addService(new BankService())
                .build();

        try {
            server.start();
            System.out.println(" Server started at port 8089!");
            server.awaitTermination();
        } catch(Exception e) {
            System.out.println("Something went wrong" + e.toString());
        }

    }
}


