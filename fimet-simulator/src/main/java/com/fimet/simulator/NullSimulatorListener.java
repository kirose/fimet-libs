package com.fimet.simulator;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;

public class NullSimulatorListener implements ISimulatorListener {

	public static final ISimulatorListener INSTANCE = new NullSimulatorListener();

	@Override
	public byte[] onSimulatorWriteMessage(ISimulator simulator, IMessage message) {
		return simulator.getParser().formatMessage(message);
	}

	@Override
	public void onSimulatorReadMessage(ISimulator simulator, IMessage message, byte[] bytes) {}

}
