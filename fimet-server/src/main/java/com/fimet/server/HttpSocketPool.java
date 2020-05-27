package com.fimet.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.Manager;

public class HttpSocketPool {
	ServerSocket server;
	List<HttpSocket> sockets;
	HttpCommandServer commandServer;
	int size;
	int port;
	public HttpSocketPool(HttpCommandServer commandServer) {
		this.commandServer = commandServer;
		size = Manager.getPropertyInteger("command.server.poolSize", 2);
		port = Manager.getPropertyInteger("command.server.port", 80);
		if (size <= 0) {
			throw new FimetException("command.server.poolSize cannot be negative or zero"); 
		}
		sockets = new ArrayList<HttpSocket>(size);
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			throw new FimetException(e);
		}
		for (int i = 0; i < size; i++) {
			sockets.add(new HttpSocket(this, i));
		}
		FimetLogger.debug(HttpSocketPool.class, "HttpSocketPool initialized with "+size+" threads");
	}
	public Socket newSocket() throws IOException {
		Socket socket = server.accept();
		return socket;
	}
	public String toString() {
		return "HttpSocketPool "+sockets.size()+" "+server.getLocalSocketAddress() + " "+server.getLocalPort();
	}
//	private void close() {
//		if (server != null) {
//			try {
//				server.close();
//				System.out.println("server.closed:"+server.isClosed());
//			} catch(Throwable t) {
//				FimetLogger.error("HttpSocketThread Error", t);
//			}
//		}
//	}
}
