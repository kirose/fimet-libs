package com.fimet.simulator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;
import com.fimet.simulator.PSimulator;
import com.fimet.simulator.field.*;
import com.fimet.xml.FimetXml;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorManager extends AbstractManager implements ISimulatorManager {
	static final int DEFAULT_NUMBER_OF_THREADS = 5;
	private Map<Integer, ISimulator> simulators = new ConcurrentHashMap<>();
	private Map<String, PSimulator> externals = new ConcurrentHashMap<>();
	private SimulatorThread next;
	private SimulatorThread head;
	private SimulatorStoreWrapper store;
	public SimulatorManager() {
		ISimulatorStore wrapped = Manager.getExtension(ISimulatorStore.class, NullSimulatorStore.INSTANCE);
		store = new SimulatorStoreWrapper(wrapped);
		loadExternals();
		initThreads();
	}
	private void loadExternals() {
		FimetXml model = Manager.getModel();
		List<PSimulator> pSimulators = model.getPSimulators();
		if (pSimulators != null) {
			for (PSimulator p : pSimulators) {
				externals.put(p.getExternalId(), p);
			}
		}
	}
	private void initThreads() {
		Integer numberOfThreads = Manager.getPropertyInteger("simulator.numberOfThreads", DEFAULT_NUMBER_OF_THREADS);
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
		throw new FimetException("Unkown simulator with external id "+externalId);
	}
	@Override
	public ISimulator getSimulator(PSimulator simulator) {
		if (simulator.getExternalId() != null) {
			simulator = externals.get(simulator.getExternalId());
		}
		if (simulators.containsKey(simulator.hashCode())) {
			return simulators.get(simulator.hashCode());	
		} else {
			Simulator s = new Simulator(simulator);
			s.setStore(store);
			simulators.put(simulator.hashCode(), s);
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
	public void setStore(ISimulatorStore store) {
		this.store.setWrapped(store);
	}
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
