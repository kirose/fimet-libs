package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.ESimulatorModelXml;

@XmlRootElement(name="simulatorModels")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorModelsXml {
	@XmlElement(name="model")
	private List<ESimulatorModelXml> models;
	public SimulatorModelsXml() {
		super();
	}
	public SimulatorModelsXml(List<ESimulatorModelXml> simulatorModels) {
		super();
		this.models = simulatorModels;
	}
	public List<ESimulatorModelXml> getSimulatorModels() {
		return models;
	}
	public void setSimulatorModels(List<ESimulatorModelXml> simulatorModels) {
		this.models = simulatorModels;
	}
}
