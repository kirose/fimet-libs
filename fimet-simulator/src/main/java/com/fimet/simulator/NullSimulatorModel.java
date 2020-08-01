package com.fimet.simulator;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorModel;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class NullSimulatorModel implements ISimulatorModel {
	public static final ISimulatorModel INSTANCE = new NullSimulatorModel();
	private String name;
	public void free() {
	}
	public NullSimulatorModel() {
		this.name = "None";
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public IMessage simulateRequest(IMessage message) {
		return null;
	}
	@Override
	public IMessage simulateResponse(IMessage message) {
		return message;
	}
}
