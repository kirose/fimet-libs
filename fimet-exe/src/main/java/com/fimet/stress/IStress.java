package com.fimet.stress;

import java.io.File;
import java.util.Map;

import com.fimet.exe.StressResult;
import com.fimet.net.IMultiConnectable;
import com.fimet.simulator.ISimulator;

public interface IStress extends IMultiConnectable {
	public String getName();
	public int getCycleTime();
	public int getMessagesPerCycle();
	public Map<ISimulator, File> getStressFiles();
	public long getMaxExecutionTime();
	public StressResult getResult();
}
