package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public interface ISimulatorListener {
	byte[] onSimulatorWriteMessage(ISimulator simulator, Message message);
	void onSimulatorReadMessage(ISimulator simulator, Message message, byte[] bytes);
}
