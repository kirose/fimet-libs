package com.fimet.simulator;

import com.fimet.FimetException;

public class SimulatorThreadPool {
	private SimulatorThread next;
	private SimulatorThread head;
	public SimulatorThreadPool(int numberOfThreads) {
		//Integer numberOfThreads = Manager.getPropertyInteger("simulator.number-of-threads", 5);
		if (numberOfThreads <= 0) {
			throw new FimetException("Expected a positive numberOfThreads: "+numberOfThreads);
		}
		next = head = new SimulatorThread(0);
		if (numberOfThreads > 1) {
			SimulatorThread prev = head;
			for (int i = 1; i < numberOfThreads; i++) {
				next = new SimulatorThread(i);	
				prev.next = next;
				prev = next;
			}
		}
		next.next = head;
		next = head;
	}
	synchronized public ISimulatorThread getNextSimulatorThread() {
		return next = next.next;
	}
}
