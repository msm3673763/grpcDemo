package com.msm.grpc.helloworld;

import java.util.concurrent.TimeUnit;

import com.msm.grpc.helloworld.GreeterGrpc.GreeterBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {
	
	private final ManagedChannel channel;
	private final GreeterBlockingStub stub;
	
	public HelloWorldClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext(true).build();
		stub = GreeterGrpc.newBlockingStub(channel);
	}
	
	public void shutdown() throws Exception {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public void greet(String name) {
		HelloRequest request = HelloRequest.newBuilder().setName(name).build();
		HelloReply reply = stub.sayHello(request);
		System.out.println(reply.getMessage());
	}
	
	public static void main(String[] args) {
		HelloWorldClient client = new HelloWorldClient("127.0.0.1", 50051);
		for (int i=0;i<5;i++) {
			client.greet("world" + i);
		}
	}
}
