package com.fimet.stress;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;


import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.Paths;
import com.fimet.exe.SocketResult;
import com.fimet.exe.Task;

public class StressStoreCsv implements IStressStore {
	private static Logger logger = LoggerFactory.getLogger(StressStoreCsv.class);
	private File fileCycle;
	private File fileGlobal;
	private OutputStreamWriter outCycle;
	private OutputStreamWriter outGlobal;
	private File cycleOutputFile;
	private File globalOutputFile;
	public StressStoreCsv() {
		this((File)null, (File)null);
	}
	public StressStoreCsv(String pathCycle) {
		this(new File(pathCycle));
	}
	public StressStoreCsv(File cycleOutputFile) {
		this(cycleOutputFile,null);
	}
	public StressStoreCsv(String cycleOutputPath, String globalOutputPath) {
		this(cycleOutputPath != null ? new File(cycleOutputPath) : null,
			globalOutputPath != null ? new File(globalOutputPath) : null);
	}
	public StressStoreCsv(File cycleOutputFile, File globalOutputFile) {
		this.cycleOutputFile = cycleOutputFile;
		this.globalOutputFile = globalOutputFile;
	}
	public void start() {
		this.fileCycle = cycleOutputFile != null ? cycleOutputFile : new File(Paths.STORE, "stress-cycle-results.csv");
		this.fileGlobal = globalOutputFile != null ? globalOutputFile : new File(Paths.STORE, "stress-final-results.csv");
		open();
		writeHeaders();
	}
	private void writeHeaders() {
		try {
			outCycle.write(SocketResult.getHeadersCycleStats()+System.lineSeparator());
			outCycle.flush();
		} catch (IOException e) {
			logger.error("Error writing cycle results", e);
		}
		if (outGlobal != null) {
			try {
				outGlobal.write(SocketResult.getHeadersGlobalStats()+System.lineSeparator());
				outGlobal.flush();
			} catch (IOException e) {
				logger.error("Error writing cycle results", e);
			}
		}
	}
	@Override
	public void storeCycleResults(SocketResult result) {
		try {
			String line = result.getCycleStats()+"\n";
			outCycle.write(line);
			outCycle.flush();
		} catch (IOException e) {
			logger.error("Error writing cycle results", e);
		}
	}

	@Override
	public void storeGlobalResults(SocketResult result) {
		if (fileGlobal != null) {
			try {
				String line = result.getGlobalStats()+"\n";
				outGlobal.write(line);
				outGlobal.flush();
			} catch (IOException e) {
				logger.error("Error writing cycle results", e);
			}
		}
	}
	private void open() {
		try {
			outCycle = new java.io.FileWriter(fileCycle);
		} catch (IOException e) {
			throw new FimetException(e);
		}
		if (fileGlobal != null) {
			try {
				outGlobal = new java.io.FileWriter(fileGlobal);
			} catch (IOException e) {
				throw new FimetException(e);
			}
		}
	}
	public void close() {
		if (outCycle != null) {
			try {
				outCycle.close();
				outCycle = null;
			} catch (IOException e) {}
		}
		if (outGlobal != null) {
			try {
				outGlobal.close();
				outGlobal = null;
			} catch (IOException e) {}
		}
	}
	public void init(Task task, Object... params) {
		
	}
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
}
