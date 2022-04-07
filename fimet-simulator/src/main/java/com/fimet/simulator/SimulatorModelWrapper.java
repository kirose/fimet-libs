package com.fimet.simulator;

import com.fimet.parser.IMessage;

public class SimulatorModelWrapper implements ISimulatorModel {
	ISimulatorModel wrapped;
	public SimulatorModelWrapper(ISimulatorModel model) {
		wrapped = model;
	}
	
	public ISimulatorModel getWapped() {
		return wrapped;
	}

	public void setWapped(ISimulatorModel wrapped) {
		this.wrapped = wrapped!=null?wrapped:NullSimulatorModel.INSTANCE;
	}

	@Override
	public String getName() {
		return wrapped.getName();
	}

	@Override
	public IMessage simulateRequest(IMessage message) {
		return wrapped.simulateRequest(message);
	}

	@Override
	public IMessage simulateResponse(IMessage message) {
		return wrapped.simulateResponse(message);
	}
	public String toString() {
		return wrapped.getName();
	}
}
