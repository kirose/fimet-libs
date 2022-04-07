package com.fimet.simulator;

import com.fimet.parser.IMessage;

public abstract class AbstractSimulatorModel implements ISimulatorModel {
	private String name;
	public AbstractSimulatorModel(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	protected IMessage cloneMessage(IMessage m) {
		try {
			return m.clone();
		} catch (CloneNotSupportedException e) {
			throw new SimulatorException(e);
		}
	}
	protected String createMtiResponse(String mti) {
		int mtiInt = Integer.parseInt(mti);
		return String.format("%04d", mtiInt%2 == 1 ? (mtiInt+9) : (mtiInt+10));
	}
}
