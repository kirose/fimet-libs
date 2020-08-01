package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.ESimulatorModel;

@XmlRootElement(name="simulatorModels")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorModelsXml {
	@XmlElement(name="model")
	private List<ESimulatorModel> models;
	public SimulatorModelsXml() {
		super();
	}
	public SimulatorModelsXml(List<ESimulatorModel> simulatorModels) {
		super();
		this.models = simulatorModels;
	}
	public List<ESimulatorModel> getSimulatorModels() {
		return models;
	}
	public void setSimulatorModels(List<ESimulatorModel> simulatorModels) {
		this.models = simulatorModels;
	}
}
