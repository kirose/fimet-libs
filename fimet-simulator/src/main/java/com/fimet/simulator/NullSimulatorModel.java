package com.fimet.simulator;


import com.fimet.iso8583.parser.Message;
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
	public Message simulateResponse(Message message) {
		return null;
	}
	public Message simulateRequest(Message message) {
		return message;
	}
	@Override
	public String getName() {
		return name;
	}
}
