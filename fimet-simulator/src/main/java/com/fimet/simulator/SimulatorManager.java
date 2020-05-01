package com.fimet.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.commons.exception.FimetException;
import com.fimet.commons.exception.SimulatorException;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.entity.sqlite.ESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;
import com.fimet.simulator.PSimulator;
import com.fimet.xml.FimetXml;
import com.fimet.xml.SimulatorXml;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorManager implements ISimulatorManager {
	static final int NUMBER_OF_THREADS = 5;	
	private ISimulatorModelManager simulatorModelManager = Manager.get(ISimulatorModelManager.class);
	private Map<Integer, ISimulator> simulators = new HashMap<>();
	private Map<Integer, Integer> mapIdToHash = new HashMap<>();
	private Map<String, PSimulator> externals = new HashMap<>();
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
			if (simulator.getModel() == null && simulator.getIdSimulator() == null) {
				simulator.setIdSimulator(-1);
			} else {
				ESimulator entity = simulatorModelManager.getEntity(simulator.getModel());
				if (entity == null) {
					throw new SimulatorException("Unkown simulator model "+simulator.getModel());
				}
				simulator.setIdSimulator(entity.getId());
			}
			simulators.put(simulator.hashCode(), new Simulator(simulator));
			mapIdToHash.put(simulator.getIdSimulator(), simulator.hashCode());
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
	public void freeSimulator(int id) {
		if (mapIdToHash.containsKey(id)) {
			simulators.get(mapIdToHash.get(id)).free();
		}
	}
	@Override
	public void free() {
		for (java.util.Map.Entry<Integer, ISimulator> e : simulators.entrySet()) {
			e.getValue().free();
		}
	}
	@Override
	public void saveState() {}
	@Override
	public void freeSimulators(List<Integer> ids) {
		for (Integer id : ids) {
			freeSimulator(id);
		}
	}
}
