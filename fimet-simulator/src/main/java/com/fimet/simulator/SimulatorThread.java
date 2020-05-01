package com.fimet.simulator;

import java.util.concurrent.LinkedBlockingQueue;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;

public class SimulatorThread extends Thread implements ISimulatorThread {
	SimulatorThread next;
	private boolean alive;
	private LinkedBlockingQueue<Node> queue;
	public SimulatorThread(int index) {
		super("SimulatorThread-"+index);
		queue = new LinkedBlockingQueue<Node>();
		alive = true;
		start();
	}
	public void simulateRead(ISimulator s, byte[] message) {
		queue.add(new Node(Type.READ, s, message));
	}
	public void simulateWrite(ISimulator s, Message message) {
		queue.add(new Node(Type.WRITE, s, message));
	}
	public void run() {
		try {
			Node next;
			while (alive) {
				next = queue.take();
				switch (next.type) {
				case READ:
					next.simulator.doReadMessage((byte[])next.message);
					break;
				case WRITE:
					next.simulator.doWriteMessage((Message)next.message);
					break;
				default:
					throw new FimetException("Invalid messenger operation type "+next.type);
				}
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
		}
	}
	private class Node {
		Type type;
		volatile ISimulator simulator;
		volatile Object message;
		public Node(Type type, ISimulator s, Object message) {
			super();
			this.type = type;
			this.simulator = s;
			this.message = message;
		}
	}
	enum Type {
		READ, WRITE
	}
}
