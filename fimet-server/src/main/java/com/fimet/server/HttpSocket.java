package com.fimet.server;

import java.io.IOException;
import java.net.Socket;

import com.fimet.FimetLogger;
import com.fimet.socket.AdaptedSocket;
import com.fimet.socket.SocketException;

public class HttpSocket {

	public static enum Status {
		DISCONNECTED, CONNECTING, CONNECTED
	}
	
	Socket socket;
	HttpSocketPool pool;
	Status status;
	int socketNumber;
	HttpSocketThread thread;
	HttpParser parser;
	public HttpSocket(HttpSocketPool pool, int socketNumber) {
		this.socketNumber = socketNumber;
		this.pool = pool;
		status = Status.DISCONNECTED;
		parser = new HttpParser();
		thread = new HttpSocketThread();
		thread.start();
	}
	public synchronized boolean isDisconnected() {
		return status == Status.DISCONNECTED;
	}
	public synchronized boolean isConnected() {
		return status == Status.CONNECTED;
	}
	public synchronized boolean isConnecting() {
		return status == Status.CONNECTING;
	}
	@Override
	public String toString() {
		return "HttpSocket";
	}
	public void reply(HttpMessage message) {
		try {
			if (socket == null) {
				throw new SocketException("Socket is disconnected cannot write data: "+message);
			}
			parser.format(this, message);
			if (FimetLogger.isEnabledDebug()) {
				FimetLogger.debug(AdaptedSocket.class,"Written  to "+ this+"\n"+message);
			}
		} catch (IOException e) {
			FimetLogger.error("Fail at reply message "+ message, e);
			try {socket.close();} catch (Throwable t) {}
			throw new SocketException(e);
		}
		closeSocket();
	}
	class HttpSocketThread extends Thread {
		boolean alive = true;
		HttpSocketThread(){
			setName("HttpSocket-"+socketNumber+" "+pool.server.getLocalSocketAddress()+" "+pool.port);
		}
		public void run() {
			while (alive) {
				try {
					if (socket == null) {
						status = Status.CONNECTING;
						socket = pool.newSocket();
						status = Status.CONNECTED;
					}
					HttpMessage message = parser.parse(socket.getInputStream());
					if (message == null) {
						closeSocket();
					} else {
						message.setSocket(HttpSocket.this);
						pool.commandServer.onHttpRequest(message);
					}
				} catch (IOException se) {// Socket closed
					closeSocket();
				} catch (Throwable e) {// Fallo la conexion
					FimetLogger.error("HttpSocketThread Error", e);
					closeSocket();
				}
			}
		}
	}
	private void closeSocket() {
		if (socket != null) {
			try {
				Socket s = socket;
				socket = null;
				s.close();
				status = Status.DISCONNECTED;
			} catch (Throwable t) {}
		}
	}
	public void doWrite(byte[] message) throws IOException {
		synchronized (this) {
			socket.getOutputStream().write(message);
		}
	}
	public void doWait() throws InterruptedException {
		if (thread.getState() != Thread.State.WAITING) {
			synchronized (thread) {
				thread.wait();
			}
		}
	}
	public void doNotify() {
		if (thread != null && thread.getState() == Thread.State.WAITING) {
			synchronized (thread) {
				thread.notify();
			}
		}
	}
}
