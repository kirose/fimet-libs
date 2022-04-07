package com.fimet.simulator;

import com.fimet.parser.IMessage;

public class NullSimulatorStore implements ISimulatorStore {
	public static final ISimulatorStore INSTANCE = new NullSimulatorStore();
	@Override
	public void storeIncoming(ISimulator simulator, IMessage inMsg, byte[] inBytes) {}

	@Override
	public void storeOutgoing(ISimulator simulator, IMessage outMsg, byte[] outBytes) {}
}
