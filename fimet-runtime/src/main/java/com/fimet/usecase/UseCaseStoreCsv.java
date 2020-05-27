package com.fimet.usecase;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.IStoreTask;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseResult;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.socket.ISocket;
import com.fimet.stress.StressStoreCsv;

public class UseCaseStoreCsv implements IUseCaseStore {
	private File useCaseFile;
	private OutputStreamWriter useCaseWriter;
	public UseCaseStoreCsv() {
		this.useCaseFile = new File(Task.STORE_PATH, "usecase.csv");
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
			FimetLogger.error(StressStoreCsv.class, "Error writing usecase log:"+useCaseFile.getName(), e);
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
		for (Map.Entry<ISimulator, ValidationResult[]> e : r.getSimulatorValidations().entrySet()) {
			if (e.getValue() != null && e.getValue().length > 0) {
				s.append(",\"[").append(e.getKey().getModel().getName()+"-"+e.getKey().getSocket().getPort()).append("]:");
				for (ValidationResult v : e.getValue()) {
					s.append(v.getValidation().replace('"', '\''))
					.append(":").append(v.isSuccess() ? "SUCCESS" : "FAIL").append(",");
					if (v.isFail()) {
						valid = false;
					}
				}
				s.delete(s.length()-1, s.length());
				s.append("\"");
			}
		}
		return new Timestamp(r.getStartTime())+","+(r.getFinishTime()-r.getStartTime())+","+r.getStatus()+","+uc.getName()+","+(valid?"VALID":"INVALID")+s.toString();
	}
	public class UseCaseStoreTask implements IStoreTask {
		long start;
		long end;
		public File pack() {
			return null;
		}
		public void clean() {
			
		}
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
	public void open(UUID idTask) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void close(UUID idTask) {
		// TODO Auto-generated method stub
		
	}
}
