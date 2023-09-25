package com.example.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.HelloRequest;
import proto.HelloResponse;
import proto.HelloWorldServiceGrpc;

import java.util.Scanner;

public class SimpleRPC {

    public static void main(String[] args) throws InterruptedException {
        // Crie um canal gRPC para se conectar ao servidor
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090) // Host e porta do servidor gRPC
                .usePlaintext() // Usar comunicação não segura (somente para fins de demonstração)
                .build();

        System.out.println("------------------------ ");
        System.out.println("* Channel Open * ");
        HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub = HelloWorldServiceGrpc.newBlockingStub(channel);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite uma mensagem para enviar ao servidor: ");

        HelloRequest request = HelloRequest.newBuilder().setName(scanner.nextLine()).build();
        HelloResponse response = blockingStub.sayHello(request);

        System.out.println(String.format("Response from gRPC server: [%s]", response.getGreeting()));
        System.out.println("* Channel Closed * ");

        scanner.close();
        channel.shutdown();
        Thread.sleep(1000);
    }
}
