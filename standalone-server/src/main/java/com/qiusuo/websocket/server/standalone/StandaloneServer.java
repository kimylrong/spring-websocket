package com.qiusuo.websocket.server.standalone;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

/**
 * Hello world!
 *
 */
public class StandaloneServer {
	public static void main(String[] args) {
		Server server = new Server("localhost", 8025, "/websocket", null, EchoServerEndpoint.class);

		try {
			server.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please press a key to stop the server.");
			reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}
}
