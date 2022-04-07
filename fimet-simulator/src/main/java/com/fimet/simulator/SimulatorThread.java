package com.fimet.simulator;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;

public class SimulatorThread extends Thread implements ISimulatorThread {
	private static Logger logger = LoggerFactory.getLogger(SimulatorThread.class);
	SimulatorThread next;
	private boolean alive;
	private LinkedBlockingQueue<Operation> queue;
	public SimulatorThread(int index) {
		super("SimulatorThread-"+index);
		queue = new LinkedBlockingQueue<Operation>();
		alive = true;
		start();
	}
	public void simulateRead(ISimulator s, byte[] message) {
		queue.add(new Operation(OperationType.READ, s, message));
	}
	public void simulateWrite(ISimulator s, IMessage message) {
		queue.add(new Operation(OperationType.WRITE, s, message));
	}
	public void run() {
		try {
			Operation next;
			while (alive) {
				next = queue.take();
				switch (next.type) {
				case READ:
					next.simulator.doReadMessage((byte[])next.message);
					break;
				case WRITE:
					next.simulator.doWriteMessage((IMessage)next.message);
					break;
				default:
					throw new FimetException("Invalid operation type "+next.type);
				}
			}
		} catch (Throwable e) {
			logger.error("Simulator processing error", e);
		}
	}
	private class Operation {
		OperationType type;
		volatile ISimulator simulator;
		volatile Object message;
		public Operation(OperationType type, ISimulator s, Object message) {
			super();
			this.type = type;
			this.simulator = s;
			this.message = message;
		}
	}
	enum OperationType {
		READ, WRITE
	}
}
