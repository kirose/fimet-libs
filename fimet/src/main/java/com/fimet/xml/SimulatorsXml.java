package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="simulator")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorsXml {
	@XmlElement(name="simulator")
	private List<SimulatorXml> simulator;
	public SimulatorsXml() {
		super();
	}
	public SimulatorsXml(List<SimulatorXml> managers) {
		super();
		this.simulator = managers;
	}
	public List<SimulatorXml> getSimulators() {
		return simulator;
	}
	public void setSimulators(List<SimulatorXml> managers) {
		this.simulator = managers;
	}
}
