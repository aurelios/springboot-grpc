package com.example.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.HelloRequest;
import proto.HelloResponse;
import proto.HelloWorldServiceGrpc;

import java.util.Scanner;

public class BidirectionalStreamingClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        System.out.println("------------------------ ");
        System.out.println("* Channel Open * ");
        HelloWorldServiceGrpc.HelloWorldServiceStub stub = HelloWorldServiceGrpc.newStub(channel);

        StreamObserver<HelloRequest> requestObserver = stub.bidiStream(new StreamObserver<HelloResponse>() {
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
                System.out.println("Completado");
                channel.shutdown();
                System.out.println("* Channel Closed * ");
            }
        });

        // Iniciar thread para ler mensagens do usuário
        System.out.println("Digite uma mensagem para enviar ao servidor (ou 'exit' para sair): ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine();
            if ("exit".equalsIgnoreCase(userInput)) {
                requestObserver.onCompleted();
                break;
            }
            HelloRequest request = HelloRequest.newBuilder().setName(userInput).build();
            requestObserver.onNext(request);
        }
        scanner.close();
    }
}
