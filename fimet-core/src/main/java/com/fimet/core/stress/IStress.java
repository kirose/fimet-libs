package com.fimet.core.stress;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fimet.core.net.Connector.IConnectable;
import com.fimet.core.net.ISocket;

public interface IStress extends IConnectable {
	public String getName();
	public int getCycleTime();
	public int getMessagesPerCycle();
	public Map<ISocket, File> getStressFiles();
	public File getStressFile(ISocket socket);
	public List<ISocket> getConnections();
	public long getMaxExecutionTime();
}
