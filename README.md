# Spring Boot + gRPC

https://grpc.io/

## 🚀 Para rodar a app na sua máquina

1. Inicie o servidor gRPC:
```
./gradlew -p grpc-server bootRun 
```

2. Execute os clientes de exemplo:
```
./gradlew -p grpc-client runSimpleRPC 
```
```
./gradlew -p grpc-client runBidirectionalStreaming 
```
```
./gradlew -p grpc-client runStreaming
```