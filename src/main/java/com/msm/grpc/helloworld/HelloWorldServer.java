package com.msm.grpc.helloworld;

import java.io.IOException;

import com.msm.grpc.helloworld.GreeterGrpc.GreeterImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class HelloWorldServer {

	private int port = 50051;
    private Server server;
    
    /**
     * 开始方法
     * @throws IOException
     */
    private void start() throws IOException {
    	server = ServerBuilder.forPort(port).addService(new GreeterImpl())
    			.build().start();
    	
    	System.out.println("service start...");
    	
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		
    		@Override
    		public void run() {
    			System.err.println("*** shutting down gRPC server since JVM is shutting down");
    			HelloWorldServer.this.stop();
    			System.err.println("*** server shutdown ***");
    		}
    	});
    }
    
    private void stop() {
    	if (server != null) {
    		server.shutdown();
    	}
    }
    
    private void blockUntilShutdown() throws InterruptedException {
    	if (server != null) {
    		server.awaitTermination();
    	}
    }
    
    private class GreeterImpl extends GreeterImplBase {
    	
    	@Override
    	public void sayHello(HelloRequest request, StreamObserver<HelloReply> response) {
    		System.out.println("service:" + request.getName());
    		HelloReply reply = HelloReply.newBuilder().setMessage("hello " 
    				+ request.getName()).build();
    		response.onNext(reply);
    		response.onCompleted();
    	}
    }
    
    public static void main(String[] args) throws Exception {
		final HelloWorldServer server = new HelloWorldServer();
		server.start();
		server.blockUntilShutdown();
	}
}
