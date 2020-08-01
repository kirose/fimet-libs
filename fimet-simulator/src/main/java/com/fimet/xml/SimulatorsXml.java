package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.ESimulator;

@XmlRootElement(name="simulators")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorsXml {
	@XmlElement(name="simulator")
	private List<ESimulator> simulators;
	public SimulatorsXml() {
		super();
	}
	public SimulatorsXml(List<ESimulator> simulators) {
		super();
		this.simulators = simulators;
	}
	public List<ESimulator> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<ESimulator> simulators) {
		this.simulators = simulators;
	}
}
