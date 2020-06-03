package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.ESimulatorModel;
import com.fimet.simulator.IESimulator;

@XmlRootElement(name="simulator")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorsXml {
	@XmlElement(name="simulator")
	private List<IESimulator> simulator;
	@XmlElement(name="model")
	private List<ESimulatorModel> model;
	public SimulatorsXml() {
		super();
	}
	public SimulatorsXml(List<IESimulator> managers) {
		super();
		this.simulator = managers;
	}
	public List<IESimulator> getSimulators() {
		return simulator;
	}
	public void setSimulators(List<IESimulator> managers) {
		this.simulator = managers;
	}
	public List<ESimulatorModel> getModels() {
		return model;
	}
	public void setModels(List<ESimulatorModel> model) {
		this.model = model;
	}
}
