package com.fimet.simulator;

import com.fimet.parser.IMessage;

public interface ISimulatorThread {
	void simulateRead(ISimulator messenger, byte[] bytes);
	void simulateWrite(ISimulator messenger, IMessage msg);
}
