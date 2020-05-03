package com.fimet.simulator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.commons.exception.FimetException;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;
import com.fimet.simulator.PSimulator;
import com.fimet.simulator.field.IfHasSetAmount;
import com.fimet.simulator.field.IfHasSetCorrectPanLastDigit;
import com.fimet.simulator.field.IfHasSetEntryMode;
import com.fimet.simulator.field.IfHasSetModuloExtranjero;
import com.fimet.simulator.field.IfHasSetNewDateMMdd;
import com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss;
import com.fimet.simulator.field.IfHasSetNewDatehhmmss;
import com.fimet.simulator.field.IfHasSetNewDateyyMMddhhmmss;
import com.fimet.simulator.field.IfHasSetPanLast4Digits;
import com.fimet.simulator.field.IfHasSetRRN;
import com.fimet.simulator.field.IfHasSetRandom12AN;
import com.fimet.simulator.field.IfHasSetRandom12N;
import com.fimet.simulator.field.IfHasSetRandom6AN;
import com.fimet.simulator.field.IfHasSetRandom6N;
import com.fimet.simulator.field.SetNewDateMMdd;
import com.fimet.simulator.field.SetNewDateMMddhhmmss;
import com.fimet.simulator.field.SetNewDatehhmmss;
import com.fimet.simulator.field.SetNewDateyyMMddhhmmss;
import com.fimet.simulator.field.SetRandom12AN;
import com.fimet.simulator.field.SetRandom12N;
import com.fimet.simulator.field.SetRandom15N;
import com.fimet.simulator.field.SetRandom6AN;
import com.fimet.simulator.field.SetRandom6N;
import com.fimet.simulator.field.SetRandom9N;
import com.fimet.xml.FimetXml;
import com.fimet.xml.SimulatorXml;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorManager implements ISimulatorManager {
	static final int NUMBER_OF_THREADS = 5;	
	private Map<Integer, ISimulator> simulators = new ConcurrentHashMap<>();
	private Map<String, PSimulator> externals = new ConcurrentHashMap<>();
	private SimulatorThread next;
	private SimulatorThread head;
	public SimulatorManager() {
		loadExternals();
		initThreads();
	}
	private void loadExternals() {
		FimetXml model = Manager.getModel();
		if (model.getSimulators() != null && model.getSimulators().getSimulators() != null) {
			for (SimulatorXml s : model.getSimulators().getSimulators()) {
				externals.put(s.getId(), s.toPSimulator());
			}
		}
	}
	private void initThreads() {
		Integer numberOfThreads = Manager.getPropertyInteger("simulator.numberOfThreads");
		if (numberOfThreads == null) {
			numberOfThreads = NUMBER_OF_THREADS;
		}
		next = head = new SimulatorThread(0);
		if (numberOfThreads > 1) {
			SimulatorThread prev = head;
			for (int i = 1; i < numberOfThreads; i++) {
				next = new SimulatorThread(i);	
				prev.next = next;
				prev = next;
			}
		}
		next.next = head;
		next = head;
	}
	@Override
	public ISimulator getSimulator(String externalId) {
		if (externals.containsKey(externalId)) {
			PSimulator p = externals.get(externalId);
			return getSimulator(p);
		}
		throw new FimetException("Unkown simulator identified by "+externalId);
	}
	@Override
	public ISimulator getSimulator(PSimulator simulator) {
		if (simulator.getExternalId() != null) {
			simulator = externals.get(simulator.getExternalId());
		}
		if (simulators.containsKey(simulator.hashCode())) {
			return simulators.get(simulator.hashCode());	
		} else {
			simulators.put(simulator.hashCode(), new Simulator(simulator));
		}
		return simulators.get(simulator.hashCode());
	}
	@Override
	public ISimulator connect(PSimulator simulator) {
		ISimulator messenger = getSimulator(simulator);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(PSimulator simulator) {
		if (simulators.containsKey(simulator.hashCode())) {
			simulators.get(simulator.hashCode()).disconnect();
			simulators.remove(simulator.hashCode());
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<Integer, ISimulator> e : simulators.entrySet()) {
			e.getValue().disconnect();
		}
		simulators.clear();
	}
	@Override
	synchronized public ISimulatorThread getNextSimulatorThread() {
		return next = next.next;
	}
	@Override
	public void free() {
	}
	@Override
	public void saveState() {}
	@Override
	public Class<?>[] getSimulatorFieldClasses() {
		return new Class<?>[] {
			IfHasSetNewDateyyMMddhhmmss.class,
			IfHasSetNewDateMMddhhmmss.class,
			IfHasSetNewDatehhmmss.class,
			IfHasSetNewDateMMdd.class,
			IfHasSetAmount.class,
			IfHasSetEntryMode.class,
			IfHasSetCorrectPanLastDigit.class,
			IfHasSetPanLast4Digits.class,
			IfHasSetRRN.class,
			IfHasSetModuloExtranjero.class,
			IfHasSetRandom12N.class,
			IfHasSetRandom6N.class,
			IfHasSetRandom12AN.class,
			IfHasSetRandom6AN.class,
			SetRandom15N.class,
			SetRandom12N.class,
			SetRandom6N.class,
			SetRandom9N.class,
			SetRandom12AN.class,
			SetRandom6AN.class,
			SetNewDateyyMMddhhmmss.class,
			SetNewDateMMddhhmmss.class,
			SetNewDatehhmmss.class,
			SetNewDateMMdd.class
		};
	}
}
