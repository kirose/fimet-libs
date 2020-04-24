package com.fimet.net;

import java.util.concurrent.LinkedBlockingQueue;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerThread;

public class MessengerThread extends Thread implements IMessengerThread {
	MessengerThread next;
	private boolean alive;
	private LinkedBlockingQueue<Node> queue;
	public MessengerThread(int index) {
		super("MessengerThread-"+index);
		queue = new LinkedBlockingQueue<Node>();
		alive = true;
		start();
	}
	public void onSocketRead(IMessenger m, byte[] message) {
		queue.add(new Node(Type.READ, m, message));
	}
	public void onMessengerWrite(IMessenger m, Message message) {
		queue.add(new Node(Type.WRITE, m, message));
	}
	public void run() {
		try {
			while (alive) {
				Node next = queue.take();
				switch (next.type) {
				case READ:
					_onSocketRead(next.messenger, (byte[])next.message);
					break;
				case WRITE:
					_onMessengerWrite(next.messenger, (Message)next.message);
					break;
				default:
					throw new FimetException("Invalid messenger operation type "+next.type);
				}
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
		}
	}
	private void _onMessengerWrite(IMessenger m, Message msg) {
		try {
			msg = m.getSimulator().simulateRequest(msg);
			m.getListener().onMessengerWriteMessage(m, msg);
			byte[] iso = m.getConnection().getParser().formatMessage(msg);
			m.getListener().onMessengerWriteToSocket(m, iso);
			m.getSocket().write(iso);
		} catch (Throwable e) {
			FimetLogger.error(Messenger.class,"Error On Write message: "+msg, e);
		}
	}
	private void _onSocketRead(IMessenger m, final byte[] bytes) {
		try {
			m.getListener().onMessengerReadFromSocket(m, bytes);
			Message message = (Message)m.getConnection().getParser().parseMessage(bytes);
			message.setAdapter(m.getConnection().getAdapter());
			m.getListener().onMessengerReadMessage(m, message);
			Message response = m.getSimulator().simulateResponse(message);
			if (response != null) {
				m.getListener().onMessengerWriteMessage(m, response);
				byte[] iso = response.getParser().formatMessage(response);
				m.getListener().onMessengerWriteToSocket(m, iso);
				m.getSocket().write(iso);
			}
		} catch (Throwable e) { 
			FimetLogger.error(Manager.class, "Error On Read message("+(bytes!= null?bytes.length:0)+"):\n"+new String(bytes)+"\n",e);
		}			
	}
	private class Node {
		Type type;
		volatile IMessenger messenger;
		volatile Object message;
		public Node(Type type, IMessenger m, Object message) {
			super();
			this.type = type;
			this.messenger = m;
			this.message = message;
		}
	}
	enum Type {
		READ, WRITE
	}
}
