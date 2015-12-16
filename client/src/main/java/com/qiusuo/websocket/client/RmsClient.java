package com.qiusuo.websocket.client;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.auth.Credentials;

/**
 * Hello world!
 *
 */
public class RmsClient {
	private static String MESSAGE = "Hello World";
	private static CountDownLatch latch;

	public static void main(String[] args) throws Exception {
		latch = new CountDownLatch(1);
		

		ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create().build();
		ClientManager clientManager = ClientManager.createClient();
		clientManager.getProperties().put(ClientProperties.CREDENTIALS,
				new Credentials("admin", "8500d4ef7a2644c5bd450dfc5f2e3bd3"));
		
		clientManager.getProperties().put(ClientProperties.PROXY_URI, "http://localhost:8888");

		clientManager.connectToServer(new Endpoint() {
			@Override
			public void onOpen(Session session, EndpointConfig config) {
				session.addMessageHandler(new MessageHandler.Whole<String>() {
					public void onMessage(String message) {
						System.out.println(message);
						latch.countDown();
					}
				});

//				try {
//					session.getBasicRemote().sendText(MESSAGE);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}, clientConfig, new URI("ws://localhost:9006/rms/websocket/endpoint"));

		latch.await(100, TimeUnit.SECONDS);
	}
}
