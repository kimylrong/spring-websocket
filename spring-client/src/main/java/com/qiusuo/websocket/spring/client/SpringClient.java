package com.qiusuo.websocket.spring.client;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * Hello world!
 *
 */
public class SpringClient {

	public static void main(String[] args) throws Exception {
		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setHttpHeaderNames("Authorization");

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.set("Authorization", "Basic YWRtaW46ODUwMGQ0ZWY3YTI2NDRjNWJkNDUwZGZjNWYyZTNiZDM=");

		String url = "http://localhost:9006/rms/websocket/endpoint";
		StompSessionHandler handler = new MySessionHandler();
		ListenableFuture<StompSession> future = stompClient.connect(url, headers, handler);
		StompSession session = future.get();

		session.subscribe("/topic/19/limitalter", new StompFrameHandler() {
			@Override
			public void handleFrame(StompHeaders arg0, Object o) {
				String json = new String((byte[]) o);
				System.out.println("Stomp Subcribled Message" + json);
			}

			@Override
			public Type getPayloadType(StompHeaders arg0) {
				return byte[].class;
			}
		});

		Thread.sleep(600000);
	}

	static class MySessionHandler extends StompSessionHandlerAdapter {
		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			System.out.println("Stomp conenctd");
		}
	}

	static class MyWebSocketHandler implements WebSocketHandler {

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			System.out.println("WebSocket conenctd");
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			System.out.println(message.toString());
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			System.out.println("WebSocket Closed");
		}

		@Override
		public boolean supportsPartialMessages() {
			return false;
		}
	}
}
