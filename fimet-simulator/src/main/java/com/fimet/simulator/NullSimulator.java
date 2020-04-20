package com.fimet.simulator;


import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.simulator.ISimulator;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class NullSimulator implements ISimulator {
	public static final ISimulator INSTANCE = new NullSimulator();
	public void free() {
	}
	private final String name;
	public NullSimulator() {
		this.name = "None";
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	public Message simulate(Message message) {
		return message;
	}
	@Override
	public Integer getId() {
		return null;
	}
	@Override
	public Message simulateRequest(Message message) {
		return message;
	}
	@Override
	public Message simulateResponse(Message message) {
		return null;
	}
}
