package com.fimet.simulator;

import com.fimet.parser.IMessage;

public interface ISimulatorStore {
	void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes);
	void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes);
}
