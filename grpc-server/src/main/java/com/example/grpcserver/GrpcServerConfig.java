package com.example.grpcserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

	@Bean
	public Server grpcServer() throws IOException {
		return ServerBuilder.forPort(9090)
				.addService(new HelloWorldService())
				.build();
	}

	@Bean
	public Server startGrpcServer(Server server) throws IOException, InterruptedException {
		var sv = server.start();
		sv.awaitTermination();
		return sv;
	}
}