package com.fimet.simulator;

import java.util.Map;

import com.fimet.commons.FimetLogger;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.persistence.dao.SimulatorMessageDAO;
import com.fimet.core.simulator.ISimulator;
import com.fimet.simulator.msg.SimulatorMessage;
import com.fimet.simulator.msg.SimulatorMessageRequest;
import com.fimet.simulator.msg.SimulatorMessageResponse;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorImpl implements ISimulator {

	public void free() {
		simulatorRequest.clear();
		simulatorResponse.clear();
	}
	private final Integer idSimulator;
	private final String name;
	private Map<String, SimulatorMessage> simulatorRequest = new java.util.HashMap<>();
	private Map<String, SimulatorMessage> simulatorResponse = new java.util.HashMap<>();
	public SimulatorImpl(Integer idSimulator, String name) {
		this.idSimulator = idSimulator;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	public Integer getId() {
		return idSimulator;
	}
	private SimulatorMessage getSimulatorRequest(String mti) {
		if (simulatorRequest.containsKey(mti)) {
			return simulatorRequest.get(mti);
		}
		com.fimet.core.entity.sqlite.SimulatorMessage sm = SimulatorMessageDAO.getInstance().findRequestByMti(idSimulator, mti);
		if (sm == null) {
			simulatorRequest.put(mti, null);
			return null;
		}
		if (!sm.isRequest()) {
			return null;
		}
		simulatorRequest.put(mti, new SimulatorMessageRequest(this, sm));
		return simulatorRequest.get(mti);
	}
	private SimulatorMessage getSimulatorResponse(String mti) {
		if (simulatorResponse.containsKey(mti)) {
			return simulatorResponse.get(mti);
		}
		com.fimet.core.entity.sqlite.SimulatorMessage sm = SimulatorMessageDAO.getInstance().findResponseByMti(idSimulator, mti);
		if (sm == null) {
			simulatorResponse.put(mti, null);
			return null;
		}
		if (!sm.isResponse()) {
			return null;
		}
		simulatorResponse.put(mti, new SimulatorMessageResponse(this, sm));
		return simulatorResponse.get(mti);
	} 
	public Message simulateResponse(Message message) {
		if (message == null) {
			return null;
		}
		SimulatorMessage simulator = this.getSimulatorResponse(message.getMti());
		if (simulator == null) {
			FimetLogger.warning("Simulator Response "+name+" not configured for message: "+message.getMti());
			return null;
		} else {
			return simulator.simulate(message);
		}
	}
	public Message simulateRequest(Message message) {
		if (message == null) {
			return null;
		}
		SimulatorMessage simulator = this.getSimulatorRequest(message.getMti());
		if (simulator == null) {
			FimetLogger.warning("Simulator Request "+name+" not configured for message: "+message.getMti());
			return message;
		} else {
			return simulator.simulate(message);
		}
	}
}
