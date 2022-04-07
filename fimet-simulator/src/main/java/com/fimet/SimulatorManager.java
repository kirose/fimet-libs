package com.fimet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.AbstractManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.dao.ISimulatorDAO;
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

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
@Component
public class SimulatorManager extends AbstractManager implements ISimulatorManager {
	private static Logger logger = LoggerFactory.getLogger(SimulatorManager.class);
	@Autowired private ISocketManager socketManager;
	@Autowired private PropertiesManager properties;
	@Autowired private ISimulatorModelManager simulatorModelManager;
	@Autowired private IEventManager eventManager;
	@Autowired private ISimulatorDAO<? extends IESimulator> simulatorDAO;
	private Map<String, ISimulator> mapNameSimulator = new ConcurrentHashMap<>();
	private SimulatorThreadPool pool;
	private SimulatorStoreWrapper store;
	public SimulatorManager() {
		store = new SimulatorStoreWrapper(NullSimulatorStore.INSTANCE);
	}
	@PostConstruct
	@Override
	public void start() {
		initThreads();
		reload(false);
	}
	@Override
	public void reload() {
		reload(true);
	}
	private void reload(boolean fireEvent) {
		boolean removeExtensions = properties.getBoolean("simulator.remove-extensions",true);
		if (removeExtensions) {
			File path = new File(Paths.BIN, PACKAGE_EXTENSIONS.replace(".", File.separator));
			FileUtils.deleteFiles(path);
		}
		store.setWrapped(NullSimulatorStore.INSTANCE);
		List<? extends IESimulator> entities = simulatorDAO.findAll();
		mapNameSimulator.clear();
		if (entities!=null) {
			for (IESimulator e : entities) {
				getSimulator(e);
			}
		}
		if (fireEvent) {
			eventManager.fireEvent(SimulatorEvent.SIMULATOR_MANAGER_RELOADED, this, getSimulators());
		}
	}
	private void initThreads() {
		Integer numberOfThreads = properties.getInteger("simulator.number-of-threads", 5);
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
				logger.info("Simulator loaded {}", s);
				eventManager.fireEvent(SimulatorEvent.SIMULATOR_LOADED, this, s);
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
	public boolean connect(String name) {
		if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.get(name).connect();
			return true;
		}
		return false;
	}
	@Override
	public boolean disconnect(String name) {
		if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.get(name).disconnect();
			return true;
		}
		return false;
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
	public void connectAll() {
		for (Map.Entry<String, ISimulator> e : mapNameSimulator.entrySet()) {
			if (e.getValue().isDisconnected()) {
				e.getValue().connect();
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
			eventManager.fireEvent(SimulatorEvent.SIMULATOR_REMOVED, this, removed);
			return removed;
 		}
		return null;
	}
	@Override
	public ISimulator reload(String name) {
		ISimulator simulator = remove(name);
		if (simulator!=null) {
			simulatorModelManager.reload(simulator.getModel().getName());
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
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
