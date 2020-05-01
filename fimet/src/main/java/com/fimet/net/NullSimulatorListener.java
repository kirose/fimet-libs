package com.fimet.net;

import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;

public class NullSimulatorListener implements ISimulatorListener {

	public static final ISimulatorListener INSTANCE = new NullSimulatorListener();

	@Override
	public byte[] onSimulatorWriteMessage(ISimulator simulator, Message message) {
		return simulator.getParser().formatMessage(message);
	}

	@Override
	public void onSimulatorReadMessage(ISimulator simulator, Message message, byte[] bytes) {}

}
