package com.fimet.simulator;

import com.fimet.commons.exception.SimulatorException;
import com.fimet.iso8583.parser.Message;

public abstract class AbstractSimulatorModel implements ISimulatorModel {
	private String name;
	public AbstractSimulatorModel(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	protected Message cloneMessage(Message m) {
		try {
			return m.clone();
		} catch (CloneNotSupportedException e) {
			throw new SimulatorException(e);
		}
	}
}
