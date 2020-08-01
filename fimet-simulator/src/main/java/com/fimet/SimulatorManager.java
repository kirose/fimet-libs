package com.fimet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.AbstractManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.dao.ISimulatorDAO;
import com.fimet.dao.SimulatorXmlDAO;
import com.fimet.event.SimulatorEvent;
import com.fimet.parser.ParserException;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
import com.fimet.simulator.NullSimulatorStore;
import com.fimet.simulator.Simulator;
import com.fimet.simulator.SimulatorStoreWrapper;
import com.fimet.simulator.SimulatorThreadPool;
import com.fimet.utils.ArrayUtils;
import com.fimet.utils.FileUtils;
import static com.fimet.Paths.BIN_PATH;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorManager extends AbstractManager implements ISimulatorManager {
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	private Map<String, ISimulator> mapNameSimulator = new ConcurrentHashMap<>();
	private SimulatorThreadPool pool;
	private SimulatorStoreWrapper store;
	public SimulatorManager() {
		initThreads();
		store = new SimulatorStoreWrapper(NullSimulatorStore.INSTANCE);
	}
	@Override
	public void start() {
		reload(false);
	}
	@Override
	public void reload() {
		reload(true);
	}
	private void reload(boolean fireEvent) {
		boolean removeExtensions = Manager.getPropertyBoolean("simulator.removeExtensions",true);
		if (removeExtensions) {
			File path = new File(BIN_PATH, PACKAGE_EXTENSIONS.replace(".", File.separator));
			FileUtils.deleteFiles(path);
		}
		store.setWrapped(NullSimulatorStore.INSTANCE);
		ISimulatorDAO dao = Manager.get(ISimulatorDAO.class,SimulatorXmlDAO.class);
		List<IESimulator> entities = dao.findAll();
		mapNameSimulator.clear();
		if (entities!=null) {
			for (IESimulator e : entities) {
				getSimulator(e);
			}
		}
		if (fireEvent) {
			Manager.get(IEventManager.class).fireEvent(SimulatorEvent.SIMULATOR_MANAGER_RELOADED, this, getSimulators());
		}
	}
	private void initThreads() {
		Integer numberOfThreads = Manager.getPropertyInteger("simulator.numberOfThreads", 5);
		pool = new SimulatorThreadPool(numberOfThreads);
	}
	@Override
	public ISimulator getSimulator(String name) {
		if (mapNameSimulator.containsKey(name)) {
			return mapNameSimulator.get(name);
		} else {
			return null;
		}
	}
	@Override
	public ISimulator getSimulator(IESimulator entity) {
		if (entity.getName() != null) {
			if (mapNameSimulator.containsKey(entity.getName())) {
				return mapNameSimulator.get(entity.getName());
			} else {
				Simulator s = new Simulator(entity, pool);
				s.setStore(store);
				mapNameSimulator.put(s.getName(), s);
				Manager.get(IEventManager.class).fireEvent(SimulatorEvent.SIMULATOR_LOADED, this, s);
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
			mapNameSimulator.get(name).disconnect();
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<String, ISimulator> e : mapNameSimulator.entrySet()) {
			if (!e.getValue().isDisconnected()) {
				e.getValue().disconnect();
			}
		}
	}
	@Override
	public ISimulator remove(String name) {
		if (mapNameSimulator.containsKey(name)) {
			ISimulator removed = mapNameSimulator.remove(name);
			if (!removed.isDisconnected()) {
				removed.disconnect();
			}
			socketManager.remove(removed.getName());
			Manager.get(IEventManager.class).fireEvent(SimulatorEvent.SIMULATOR_REMOVED, this, removed);
			return removed;
 		}
		return null;
	}
	@Override
	public ISimulator reload(String name) {
		ISimulator simulator = remove(name);
		if (simulator!=null) {
			Manager.get(ISimulatorModelManager.class).reload(simulator.getModel().getName());
		}
		ISimulator reloaded = getSimulator(name);
		return reloaded; 
	}
	public void setStore(ISimulatorStore store) {
		this.store.setWrapped(store);
	}
	@Override
	public List<ISimulator> getSimulators() {
		return ArrayUtils.copyValuesAsList(this.mapNameSimulator);
	}
}
