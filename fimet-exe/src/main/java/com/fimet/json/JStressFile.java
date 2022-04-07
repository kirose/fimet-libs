package com.fimet.json;

import java.util.List;

public class JStressFile {
	private String name;
	private JSimulator simulator;
	private List<JStressFileBuilder> builders;
	public JSimulator getSimulator() {
		return simulator;
	}
	public void setSimulator(JSimulator simulator) {
		this.simulator = simulator;
	}
	public List<JStressFileBuilder> getBuilders() {
		return builders;
	}
	public void setBuilders(List<JStressFileBuilder> builders) {
		this.builders = builders;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
