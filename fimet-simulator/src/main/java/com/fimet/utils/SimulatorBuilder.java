package com.fimet.utils;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.parser.IAdapter;
import com.fimet.parser.IParser;
import com.fimet.simulator.ESimulatorXml;
import com.fimet.simulator.ISimulator;

public class SimulatorBuilder {
	ESimulatorXml entity;
	public SimulatorBuilder() {
		entity = new ESimulatorXml();
		entity.setAdapter("127.0.0.1");
	}
	public SimulatorBuilder name(String name) {
		entity.setName(name);
		return this;
	}
	public SimulatorBuilder parser(String parser) {
		entity.setParser(parser);
		return this;
	}
	public SimulatorBuilder parser(IParser parser) {
		entity.setParser(parser.getName());
		return this;
	}
	public SimulatorBuilder model(String model) {
		entity.setModel(model);
		return this;
	}
	public SimulatorBuilder address(String address) {
		entity.setAddress(address);
		return this;
	}
	public SimulatorBuilder port(int port) {
		entity.setPort(port);
		return this;
	}
	public SimulatorBuilder server(boolean server) {
		entity.setServer(server);
		return this;
	}
	public SimulatorBuilder adapter(String adapter) {
		entity.setAdapter(adapter);
		return this;
	}
	public SimulatorBuilder adapter(IAdapter adapter) {
		entity.setAdapter(adapter.getName());
		return this;
	}
	public ISimulator build() {
		return Manager.getManager(ISimulatorManager.class).getSimulator(entity);
	}
}
