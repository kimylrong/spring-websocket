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

/**
 * Hello world!
 *
 */
public class Client {
	private static String MESSAGE = "Hello World";
	private static CountDownLatch latch;

	public static void main(String[] args) throws Exception {
		latch = new CountDownLatch(1);

		ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create().build();
		ClientManager clientManager = ClientManager.createClient();

		clientManager.connectToServer(new Endpoint() {
			@Override
			public void onOpen(Session session, EndpointConfig config) {
				session.addMessageHandler(new MessageHandler.Whole<String>() {
					public void onMessage(String message) {
						System.out.println(message);
						latch.countDown();
					}
				});

				try {
					session.getBasicRemote().sendText(MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, clientConfig, new URI("ws://localhost:8025/websocket/echo"));

		latch.await(100, TimeUnit.SECONDS);
	}
}
