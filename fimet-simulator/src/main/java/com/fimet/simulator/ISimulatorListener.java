package com.fimet.simulator;

import com.fimet.parser.IMessage;

public interface ISimulatorListener {
	byte[] onSimulatorWriteMessage(ISimulator simulator, IMessage message);
	void onSimulatorReadMessage(ISimulator simulator, IMessage message, byte[] bytes);
}
