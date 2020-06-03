package com.fimet.simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.AbstractManager;
import com.fimet.IClassLoaderManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.dao.ISimulatorDAO;
import com.fimet.parser.ParserException;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorThread;
import com.fimet.simulator.field.*;
import com.fimet.utils.FileUtils;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorManager extends AbstractManager implements ISimulatorManager {

	private Map<String, ISimulator> mapNameSimulator = new ConcurrentHashMap<>();
	private ISimulatorDAO dao = Manager.getExtension(ISimulatorDAO.class,SimulatorDAO.class);
	private SimulatorThread next;
	private SimulatorThread head;
	private SimulatorStoreWrapper store;
	public SimulatorManager() {
		initThreads();
		store = new SimulatorStoreWrapper(NullSimulatorStore.INSTANCE);
	}
	@Override
	public void start() {
		reload();
	}
	@Override
	public void reload() {
		boolean removeExtensions = Manager.getPropertyBoolean("simulator.removeExtensions",true);
		if (removeExtensions) {
			File path = new File(IClassLoaderManager.BIN_PATH, PACKAGE_EXTENSIONS.replace(".", File.separator));
			FileUtils.deleteFiles(path);
		}
		ISimulatorStore wrapped = Manager.getExtension(ISimulatorStore.class, NullSimulatorStore.INSTANCE);
		store.setWrapped(wrapped);
	}
	private void initThreads() {
		Integer numberOfThreads = Manager.getPropertyInteger("simulator.numberOfThreads", 5);
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
	public ISimulator getSimulator(String name) {
		if (mapNameSimulator.containsKey(name)) {
			return mapNameSimulator.get(name);
		} else {
			IESimulator entity = findEntity(name);
			Simulator s = new Simulator(entity);
			s.setStore(store);
			mapNameSimulator.put(s.getName(), s);
			return s;
		}
	}
	@Override
	public ISimulator getSimulator(IESimulator entity) {
		if (entity.getName() != null) {
			if (mapNameSimulator.containsKey(entity.getName())) {
				return mapNameSimulator.get(entity.getName());
			} else {
				Simulator s = new Simulator(entity);
				s.setStore(store);
				mapNameSimulator.put(s.getName(), s);
				return s;
			}
		} else {
			throw new ParserException("Simulator name is null, invalid entity "+entity);
		}
	}
	@Override
	public ISimulator connect(IESimulator entity) {
		ISimulator messenger = getSimulator(entity);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(String name) {
		if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.remove(name).disconnect();
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<String, ISimulator> e : mapNameSimulator.entrySet()) {
			e.getValue().disconnect();
		}
		mapNameSimulator.clear();
	}
	@Override
	synchronized public ISimulatorThread getNextSimulatorThread() {
		return next = next.next;
	}
	@Override
	public void reload(String simulator) {
		ISimulator s = mapNameSimulator.remove(simulator);
		Manager.get(ISimulatorModelManager.class).reload(s.getModel().getName());
	}
	public void setStore(ISimulatorStore store) {
		this.store.setWrapped(store);
	}
	private IESimulator findEntity(String name) {
		IESimulator entity = dao.getByName(name);
		if (entity != null) {
			return entity;
		} else {
			throw new ParserException("Unknow simulator: "+name);
		}
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
	@Override
	public List<ISimulator> getSimulators() {
		List<IESimulator> entities = dao.getAll();
		List<ISimulator> simulators = new ArrayList<>();
		for (IESimulator e : entities) {
			simulators.add(getSimulator(e));
		}
		return simulators;
	}
}
