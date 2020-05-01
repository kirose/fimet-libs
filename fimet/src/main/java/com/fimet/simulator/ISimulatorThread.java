package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public interface ISimulatorThread {
	void simulateRead(ISimulator messenger, byte[] bytes);
	void simulateWrite(ISimulator messenger, Message msg);
}
