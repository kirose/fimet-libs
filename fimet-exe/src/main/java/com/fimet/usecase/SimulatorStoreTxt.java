package com.fimet.usecase;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.ISessionManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
import com.fimet.utils.MessageUtils;

public class SimulatorStoreTxt implements ISimulatorStore {
	private static Logger logger = LoggerFactory.getLogger(SimulatorStoreTxt.class);
	private ISessionManager sessionManager = Manager.getManager(ISessionManager.class);
	private static final File STORE_FOLDER = new File("store"); 
	private File simulatorFile;
	private OutputStreamWriter simulatorWriter;
	boolean enable;
	public SimulatorStoreTxt() {
		if (!STORE_FOLDER.exists()) {
			STORE_FOLDER.mkdirs();
		}
		this.simulatorFile = new File(STORE_FOLDER, "simulator.txt");
		try {
			simulatorWriter = new java.io.FileWriter(simulatorFile);
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {
		writeSimulatorLog("I", simulator, message, bytes);
	}

	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {
		writeSimulatorLog("O", simulator, message, bytes);
	}
	public void close() {
	}
	private void writeSimulatorLog(String inOut, ISimulator simulator,  IMessage message, byte[] bytes) {
		if (enable) {
			Session session = sessionManager.getSession(message);
			try {
				simulatorWriter.write(""
					+"["+new Timestamp(System.currentTimeMillis())+"]"
					+"["+inOut+"]"
					+"["+(session != null ? session.getUseCase().getName() : null)+"]"
					+"["+simulator.getModel().getName()+"]"
					+"["+simulator.getSocket().getPort()+"]"
					+"["+MessageUtils.toJson(message)+"]\n"
					//+"["+new String(Converter.asciiToHex(bytes))+"]\n"
				 );
				simulatorWriter.flush();
			} catch (IOException e) {
				logger.error("Error writing simulator log:"+simulatorFile.getName(), e);
			}
		}
	}
}
