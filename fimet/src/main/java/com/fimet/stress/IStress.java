package com.fimet.stress;

import java.io.File;
import java.util.Map;

import com.fimet.net.ISocket;
import com.fimet.net.MultiConnector.IMultiConnectable;

public interface IStress extends IMultiConnectable {
	public String getName();
	public int getCycleTime();
	public int getMessagesPerCycle();
	public Map<ISocket, File> getStressFiles();
	public long getMaxExecutionTime();
}
