package com.example.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.HelloRequest;
import proto.HelloResponse;
import proto.HelloWorldServiceGrpc;

import java.util.Scanner;

public class StreamingClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        Scanner scanner = new Scanner(System.in);

        StreamObserver<HelloResponse> responseObserver = new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse response) {
                System.out.println(String.format("Response: [%s]", response.getGreeting()));
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Erro: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Finished.");
            }
        };

        System.out.println("------------------------ ");
        System.out.println("* Channel Open * ");
        HelloWorldServiceGrpc.HelloWorldServiceStub stub = HelloWorldServiceGrpc.newStub(channel);

        // Iniciar thread para ler mensagens do usuário
        System.out.println("Pressione Enter para receber mensagens do servidor (ou 'exit' para sair): ");
        while (true) {
            String userInput = scanner.nextLine();
            if ("exit".equalsIgnoreCase(userInput)) {
                responseObserver.onCompleted();
                break;
            }
            HelloRequest request = HelloRequest.newBuilder().setName(userInput).build();
            stub.serverStream(request, responseObserver);
        }
        scanner.close();
        channel.shutdown();
        System.out.println("* Channel Closed * ");
        Thread.sleep(1000);
    }
}