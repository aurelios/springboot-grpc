package com.example.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.HelloRequest;
import proto.HelloResponse;
import proto.HelloWorldServiceGrpc;

import java.util.Scanner;

public class GrpcClientServiceApplication {

    public static void main(String[] args) {
        // Crie um canal gRPC para se conectar ao servidor
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090) // Host e porta do servidor gRPC
                .usePlaintext() // Usar comunicação não segura (somente para fins de demonstração)
                .build();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------ ");
            System.out.println("* Channel Open * ");
            HelloWorldServiceGrpc.HelloWorldServiceBlockingStub blockingStub = HelloWorldServiceGrpc.newBlockingStub(channel);
            System.out.println("Digite uma mensagem para enviar ao servidor (ou 'exit' para sair): ");
            String userInput = scanner.nextLine();
            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }
            HelloRequest request = HelloRequest.newBuilder().setName(userInput).build();
            HelloResponse response = blockingStub.sayHello(request);
            System.out.println(String.format("Response from gRPC server: [%s]", response.getGreeting()));
            System.out.println("* Channel Closed * ");
        }
        scanner.close();
        channel.shutdown();

    }
}
