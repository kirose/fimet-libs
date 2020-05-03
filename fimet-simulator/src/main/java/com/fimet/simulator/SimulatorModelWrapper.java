package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public class SimulatorModelWrapper implements ISimulatorModel {
	ISimulatorModel wrapped;
	public SimulatorModelWrapper(ISimulatorModel model) {
		wrapped = model;
	}
	
	ISimulatorModel getWapped() {
		return wrapped;
	}

	void setWapped(ISimulatorModel wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public String getName() {
		return wrapped.getName();
	}

	@Override
	public Message simulateRequest(Message message) {
		return wrapped.simulateRequest(message);
	}

	@Override
	public Message simulateResponse(Message message) {
		return wrapped.simulateResponse(message);
	}
	public String toString() {
		return wrapped.getName();
	}
}
