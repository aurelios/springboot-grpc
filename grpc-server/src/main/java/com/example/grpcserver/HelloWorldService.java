package com.example.grpcserver;

import io.grpc.stub.StreamObserver;
import proto.HelloRequest;
import proto.HelloResponse;
import proto.HelloWorldServiceGrpc;

public class HelloWorldService extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		System.out.println("Request received on gRPC server: " + request);

		String name = request.getName();
		String greeting = "Hello, " + name + "!";

		HelloResponse response = HelloResponse.newBuilder()
				.setGreeting(greeting)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void serverStream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		System.out.println("Streaming Request received on gRPC server: " + request);
		// Lógica para enviar uma sequência de mensagens para o cliente
		for (int i = 0; i < 5; i++) {
			HelloResponse response = HelloResponse.newBuilder()
					.setGreeting(String.format("Hello %s, Mensagem %d", request.getName(), (i+1)))
					.build();
			responseObserver.onNext(response);
			sleep(1000); // Simula um atraso de 1 segundo entre as mensagens
		}
		responseObserver.onCompleted();
	}

	private void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public StreamObserver<HelloRequest> bidiStream(StreamObserver<HelloResponse> responseObserver) {
		System.out.println("Bidirectional Streaming Request started on gRPC server !");
		return new StreamObserver<HelloRequest>() {
			@Override
			public void onNext(HelloRequest request) {
				System.out.println("Bidirectional Streaming Request received on gRPC server: " + request);
				// Lógica para processar a mensagem do cliente
				HelloResponse response = HelloResponse.newBuilder()
						.setGreeting("Hello " + request.getName())
						.build();
				responseObserver.onNext(response);
			}

			@Override
			public void onError(Throwable t) {
				// Lógica para lidar com erros
			}

			@Override
			public void onCompleted() {
				System.out.println("Bidirectional Streaming finished on gRPC server.");
				// Concluído
				responseObserver.onCompleted();
			}
		};
	}
}
