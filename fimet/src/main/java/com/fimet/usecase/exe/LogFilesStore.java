package com.fimet.usecase.exe;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.converter.Converter;
import com.fimet.commons.exception.FimetException;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.stress.exe.FileStoreResults;
import com.fimet.usecase.IUseCase;

public class LogFilesStore implements IStoreResults {
	private static final File LOGS_FOLDER = new File("logs"); 
	private File logFile;
	private File validationsFile;
	private OutputStreamWriter logWriter;
	private OutputStreamWriter validationsWriter;
	public LogFilesStore() {
		if (!LOGS_FOLDER.exists()) {
			LOGS_FOLDER.mkdirs();
		}
		this.logFile = new File(LOGS_FOLDER, "socket.log");
		this.validationsFile = new File(LOGS_FOLDER, "usecase.log");
		try {
			logWriter = new java.io.FileWriter(logFile);
		} catch (IOException e) {
			throw new FimetException(e);
		}
		try {
			validationsWriter = new java.io.FileWriter(validationsFile);
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void storeIncoming(ISimulator simulator, IUseCase useCase, ValidationResult[] validations, Message message, byte[] bytes) {
		writeLog("I", simulator, useCase, message, bytes);
		if (useCase != null && validations != null)
			writeValidations("I", simulator, useCase, validations);
	}

	@Override
	public void storeOutgoing(ISimulator simulator, IUseCase useCase, Message message, byte[] bytes) {
		writeLog("O", simulator, useCase, message, bytes);
	}
	@Override
	public void storeStart(IUseCase useCase) {
		try {
			validationsWriter.write(""
				+"["+new Timestamp(System.currentTimeMillis())+"]"
				+"[START]"
				+"["+useCase.getName()+"]\n"
			 );
			validationsWriter.flush();
		} catch (IOException e) {
			FimetLogger.error(FileStoreResults.class, "Error writing log:"+validationsFile.getName(), e);
		}
	}
	@Override
	public void storeFinish(IUseCase useCase, ExecutionResult result) {
		try {
			validationsWriter.write(""
				+"["+new Timestamp(System.currentTimeMillis())+"]"
				+"[END]"
				+"["+useCase.getName()+"]"
				+"["+result.status+"]"
				+"["+(result.getFinishTime()-result.getStatTime())+"]\n"
			 );
			validationsWriter.flush();
		} catch (IOException e) {
			FimetLogger.error(FileStoreResults.class, "Error writing log:"+validationsFile.getName(), e);
		}
	}
	private void writeLog(String inOut, ISimulator simulator, IUseCase useCase, Message message, byte[] bytes) {
		try {
			logWriter.write(""
				+"["+new Timestamp(System.currentTimeMillis())+"]"
				+"["+inOut+"]"
				+"["+(useCase != null ? useCase.getName() : null)+"]"
				+"["+simulator.getModel().getName()+"]"
				+"["+simulator.getSocket().getPort()+"]"
				+"["+message.getMti()+"]"
				+"["+new String(Converter.asciiToHex(bytes))+"]\n"
			 );
			logWriter.flush();
		} catch (IOException e) {
			FimetLogger.error(FileStoreResults.class, "Error writing log:"+validationsFile.getName(), e);
		}
	}
	private void writeValidations(String inOut, ISimulator simulator, IUseCase useCase, ValidationResult[] validations) {
		if (validations != null) {
			try {
				StringBuilder s = new StringBuilder();
				s.append("["+new Timestamp(System.currentTimeMillis())+"]");
				s.append("["+inOut+"]");
				s.append("["+(useCase != null ? useCase.getName() : null)+"]");
				s.append("["+simulator.getModel().getName()+"]");
				s.append("["+simulator.getSocket().getPort()+"]");
				s.append("[\n");
				for (ValidationResult v : validations) {
					s.append(v.toString()).append('\n');
				}
				s.append("]\n");
				validationsWriter.write(s.toString());
				validationsWriter.flush();
			} catch (IOException e) {
				FimetLogger.error(FileStoreResults.class, "Error writing log:"+logFile.getName(), e);
			}
		}
	}
}
