package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.ESimulatorXml;

@XmlRootElement(name="simulators")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorsXml {
	@XmlElement(name="simulator")
	private List<ESimulatorXml> simulators;
	public SimulatorsXml() {
		super();
	}
	public SimulatorsXml(List<ESimulatorXml> simulators) {
		super();
		this.simulators = simulators;
	}
	public List<ESimulatorXml> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<ESimulatorXml> simulators) {
		this.simulators = simulators;
	}
}
