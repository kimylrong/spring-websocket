/*
 * EchoServerEndpoint.java 2015年12月15日
 */
package com.qiusuo.websocket.server.standalone;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * An ServerEndpoint that Just echo back message
 *
 * @author Li Rong
 */
@ServerEndpoint("/echo")
public class EchoServerEndpoint {
	@OnMessage
	public String sendBackMessage(String message, Session session) {
		System.out.println(message);
		return "From Server:" + message;
	}
}
