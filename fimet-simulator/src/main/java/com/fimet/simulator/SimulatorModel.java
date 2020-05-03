package com.fimet.simulator;

import java.util.Map;

import com.fimet.commons.FimetLogger;
import com.fimet.iso8583.parser.Message;
import com.fimet.persistence.dao.SimulatorMessageDAO;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.msg.SimulatorMessage;
import com.fimet.simulator.msg.SimulatorMessageRequest;
import com.fimet.simulator.msg.SimulatorMessageResponse;

public class SimulatorModel implements ISimulatorModel {
	private Map<String, SimulatorMessage> simulatorRequest = new java.util.HashMap<>();
	private Map<String, SimulatorMessage> simulatorResponse = new java.util.HashMap<>();
	Integer idSimulator;
	String name;
	public SimulatorModel(Integer idSimulator, String name) {
		super();
		this.idSimulator = idSimulator;
		this.name = name;
	}
	public void free() {
		simulatorRequest.clear();
		simulatorResponse.clear();
	}
	private SimulatorMessage getSimulatorRequest(String mti) {
		if (simulatorRequest.containsKey(mti)) {
			return simulatorRequest.get(mti);
		}
		com.fimet.entity.sqlite.ESimulatorMessage sm = SimulatorMessageDAO.getInstance().findRequestByMti(idSimulator, mti);
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
		com.fimet.entity.sqlite.ESimulatorMessage sm = SimulatorMessageDAO.getInstance().findResponseByMti(idSimulator, mti);
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
	public Integer getId() {
		return idSimulator;
	}
	@Override
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
}
