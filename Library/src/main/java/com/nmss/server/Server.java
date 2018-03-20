package com.nmss.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import dk.i1.diameter.node.Node;
import dk.i1.diameter.node.NodeSettings;
import dk.i1.diameter.node.Peer;
import dk.i1.diameter.session.Session;
import dk.i1.diameter.session.SessionManager;

public class Server {
	public static void main(String[] args) throws IOException {

		ServerSocket server = new ServerSocket(8888);

		while (true) {
			System.out.println("Waiting for connection");

			final Socket connection = server.accept();
			System.out.println("Accepted");

			ServerRequestHandler requestHandler = new ServerRequestHandler();
			requestHandler.socket = connection;
			new Thread(requestHandler::start).start();
		}

	}
}
