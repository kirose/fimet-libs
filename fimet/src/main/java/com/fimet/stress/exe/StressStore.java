package com.fimet.stress.exe;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.exe.IStressStore;

public class StressStore implements IStressStore {
	private File fileCycle;
	private File fileGlobal;
	private OutputStreamWriter outCycle;
	private OutputStreamWriter outGlobal;
	public StressStore() {
		this((File)null, (File)null);
	}
	public StressStore(String pathCycle) {
		this(new File(pathCycle));
	}
	public StressStore(File cycleOutputFile) {
		this(cycleOutputFile,null);
	}
	public StressStore(String cycleOutputPath, String globalOutputPath) {
		this(cycleOutputPath != null ? new File(cycleOutputPath) : null,
			globalOutputPath != null ? new File(globalOutputPath) : null);
	}
	public StressStore(File cycleOutputFile, File globalOutputFile) {
		this.fileCycle = cycleOutputFile != null ? cycleOutputFile : new File("stress/stress-results-cycle.txt");
		this.fileGlobal = globalOutputFile != null ? globalOutputFile : new File("stress/stress-results-final.txt");
		open();
		writeHeaders();
	}
	private void writeHeaders() {
		try {
			outCycle.write(InjectorResult.getHeadersCycleStats()+"\n");
			outCycle.flush();
		} catch (IOException e) {
			FimetLogger.error(StressStore.class, "Error writing cycle results", e);
		}
		if (outGlobal != null) {
			try {
				outGlobal.write(InjectorResult.getHeadersGlobalStats()+"\n");
				outGlobal.flush();
			} catch (IOException e) {
				FimetLogger.error(StressStore.class, "Error writing cycle results", e);
			}
		}
	}
	@Override
	public void storeCycleResults(InjectorResult result) {
		try {
			outCycle.write(result.getCycleStats()+"\n");
			outCycle.flush();
		} catch (IOException e) {
			FimetLogger.error(StressStore.class, "Error writing cycle results", e);
		}
	}

	@Override
	public void storeGlobalResults(InjectorResult result) {
		if (fileGlobal != null) {
			try {
				outGlobal.write(result.getGlobalStats()+"\n");
				outGlobal.flush();
			} catch (IOException e) {
				FimetLogger.error(StressStore.class, "Error writing cycle results", e);
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

}
