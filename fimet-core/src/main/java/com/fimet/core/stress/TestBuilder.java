package com.fimet.core.stress;

import java.io.File;
import java.util.HashMap;

import com.fimet.core.net.ISocket;
import com.fimet.core.stress.exe.IExecutorListener;
import com.fimet.core.stress.exe.StressExecutor;

public class TestBuilder {
	private IExecutorListener listener;
	private Stress stress;
	public TestBuilder() {
		stress = new Stress();
		stress.setStressFiles(new HashMap<>());
	}
	public TestBuilder addConnections(ISocket ... sockets) {
		for (ISocket socket : sockets) {
			stress.getStressFiles().put(socket, null);
		}
		return this;
	}
	public TestBuilder addConnection(ISocket socket) {
		stress.getStressFiles().put(socket, null);
		return this;
	}
	public TestBuilder setCycleTime(int cycleTime) {
		stress.setCycleTime(cycleTime);
		return this;
	}
	public TestBuilder setMessagesPerCycle(int messagesPerCycle) {
		stress.setMessagesPerCycle(messagesPerCycle);
		return this;
	}
	public TestBuilder addSocketStress(ISocket socket, File stressFile) {
		stress.getStressFiles().put(socket, stressFile);
		return this;
	}
	public TestBuilder setExecutorListener(IExecutorListener listener) {
		this.listener = listener;
		return this;
	}
	public void execute() {
		StressExecutor executor = new StressExecutor(stress);
		executor.setListener(listener);
		executor.execute();
	}
}
