package com.fimet.core.stress.exe;

import java.io.File;
import java.io.IOException;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;

public class FileStoreResults implements IStoreResults {
	private File fileCycle;
	private File fileGlobal;
	private java.io.OutputStreamWriter outCycle;
	private java.io.OutputStreamWriter outGlobal;
	public FileStoreResults(String pathCycle) {
		this(new File(pathCycle));
	}
	public FileStoreResults(File fileCycle) {
		this(fileCycle,null);
	}
	public FileStoreResults(String pathCycle, String pathGlobal) {
		this(new File(pathCycle), pathGlobal != null ? new File(pathGlobal) : null);
	}
	public FileStoreResults(File fileCycle, File fileGlobal) {
		this.fileCycle = fileCycle;
		this.fileGlobal = fileGlobal;
		open();
		writeHeaders();
	}
	private void writeHeaders() {
		try {
			outCycle.write(InjectorResult.getHeadersCycleStats()+"\n");
			outCycle.flush();
		} catch (IOException e) {
			FimetLogger.error(FileStoreResults.class, "Error writing cycle results", e);
		}
		if (outGlobal != null) {
			try {
				outGlobal.write(InjectorResult.getHeadersGlobalStats()+"\n");
				outGlobal.flush();
			} catch (IOException e) {
				FimetLogger.error(FileStoreResults.class, "Error writing cycle results", e);
			}
		}
	}
	@Override
	public void storeCycleResults(InjectorResult result) {
		try {
			outCycle.write(result.getCycleStats()+"\n");
			outCycle.flush();
		} catch (IOException e) {
			FimetLogger.error(FileStoreResults.class, "Error writing cycle results", e);
		}
	}

	@Override
	public void storeGlobalResults(InjectorResult result) {
		if (fileGlobal != null) {
			try {
				outGlobal.write(result.getGlobalStats()+"\n");
				outGlobal.flush();
			} catch (IOException e) {
				FimetLogger.error(FileStoreResults.class, "Error writing cycle results", e);
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
