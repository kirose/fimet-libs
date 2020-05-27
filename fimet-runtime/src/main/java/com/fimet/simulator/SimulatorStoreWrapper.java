package com.fimet.simulator;

import com.fimet.parser.IMessage;

public class SimulatorStoreWrapper implements ISimulatorStore {
	volatile private ISimulatorStore wrapped;
	public SimulatorStoreWrapper(ISimulatorStore wrapped) {
		this.wrapped = wrapped;
	}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {
		wrapped.storeIncoming(simulator, message, bytes);
	}

	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {
		wrapped.storeOutgoing(simulator, message, bytes);
	}
	public ISimulatorStore getWrapped() {
		return wrapped;
	}
	public void setWrapped(ISimulatorStore wrapped) {
		this.wrapped = wrapped!=null?wrapped:NullSimulatorStore.INSTANCE;
	}

}
