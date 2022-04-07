package com.fimet.usecase;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.Paths;
import com.fimet.assertions.IAssertionResult;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseResult;
import com.fimet.net.ISocket;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;

public class UseCaseStoreCsv implements IUseCaseStore {
	private static Logger logger = LoggerFactory.getLogger(UseCaseStoreCsv.class);
	private File useCaseFile;
	private OutputStreamWriter useCaseWriter;
	public UseCaseStoreCsv() {
		this.useCaseFile = new File(Paths.STORE, "usecase.csv");
		try {
			useCaseWriter = new java.io.FileWriter(useCaseFile);
			useCaseWriter.write(getCsvHeaders()+System.lineSeparator());
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void storeUseCase(IUseCase useCase) {
		try {
			useCaseWriter.write(toCsv(useCase)+System.lineSeparator());
			useCaseWriter.flush();
		} catch (IOException e) {
			logger.error("Error writing usecase log:"+useCaseFile.getName(), e);
		}
	}
	public void close() {
		if (useCaseWriter != null) {
			try {useCaseWriter.close();} catch(Exception e) {}
			useCaseWriter = null;
		}
	}
	public String getCsvHeaders() {
		return "StartTime,TimeExecution,ExecutionStatus,UseCaseName,ValidationsStatus,Validations";
	}
	public String toCsv(IUseCase uc) {
		UseCaseResult r = uc.getResult();
		StringBuilder s = new StringBuilder();
		boolean valid = true;
		for (Map.Entry<ISimulator, IAssertionResult[]> e : r.getSimulatorValidations().entrySet()) {
			if (e.getValue() != null && e.getValue().length > 0) {
				s.append(",\"[").append(e.getKey().getModel().getName()+"-"+e.getKey().getSocket().getPort()).append("]:");
				for (IAssertionResult v : e.getValue()) {
					s.append(v.toString());
					if (!v.isCorrect()) {
						valid = false;
					}
				}
				s.delete(s.length()-1, s.length());
				s.append("\"");
			}
		}
		return new Timestamp(r.getStartTime())+","+(r.getFinishTime()-r.getStartTime())+","+r.getState()+","+uc.getName()+","+(valid?"VALID":"INVALID")+s.toString();
	}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(Task task, Object ...params) {
		// TODO Auto-generated method stub
	}
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void storeProperty(IUseCase useCase, String name, String value) {
		// TODO Auto-generated method stub
		
	}
}
