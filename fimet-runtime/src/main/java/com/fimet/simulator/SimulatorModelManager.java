package com.fimet.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.AbstractManager;
import com.fimet.IClassLoaderManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.dao.ISimulatorModelDAO;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.utils.SimulatorUtils;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorModelManager extends AbstractManager implements ISimulatorModelManager {
	private Map<String, SimulatorModelWrapper> mapNameSimulator = new HashMap<>();
	private Map<String, Class<ISimulatorModel>> classes = new HashMap<>();
	private ISimulatorModelDAO dao = Manager.getExtension(ISimulatorModelDAO.class, SimulatorModelDAO.class);
	private boolean recompileModels = Manager.getPropertyBoolean("simulatorModel.recompileModels",true);
	public SimulatorModelManager() {
		reload();
		mapNameSimulator.put("None", new SimulatorModelWrapper(NullSimulatorModel.INSTANCE));
	}
	@Override
	public void reload() {
		mapNameSimulator.clear();
		classes.clear();
		loadClases();
	}
	@SuppressWarnings("unchecked")
	private void loadClases() {
		IClassLoaderManager loaderManager = Manager.get(IClassLoaderManager.class);
		List<IESimulatorModel> entities = dao.getAll();
		for (IESimulatorModel s : entities) {
			Class<ISimulatorModel> clazz = null;
			if (recompileModels || !loaderManager.wasInstalled(s.getClassModel())) {
				clazz = SimulatorUtils.installClassSimulatorModel(s);
			} else {
				clazz = (Class<ISimulatorModel>)loaderManager.loadClass(s.getClassModel());
			}
			classes.put(s.getName(), clazz);
		}
	} 
	@Override
	public void reload(String name) {
		IESimulatorModel entity = getEntity(name);
		if (entity != null) {
			Class<ISimulatorModel> clazz = SimulatorUtils.installClassSimulatorModel(entity);
			classes.put(name, clazz);
			SimulatorModelWrapper wrapper = mapNameSimulator.get(name);
			if (wrapper != null) {
				ISimulatorModel instance;
				try {
					instance = classes.get(name).newInstance();
				} catch (Exception e) {
					throw new SimulatorException("Invalid simulator model "+name,e);
				}
				wrapper.setWapped(instance);
			}
		} else if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.remove(name).setWapped(NullSimulatorModel.INSTANCE);
		}
	}
	private IESimulatorModel getEntity(String name) {
		return dao.getByName(name);
	}
	public ISimulatorModel getSimulatorModel(String name) {
		if (mapNameSimulator.containsKey(name)) {
			return mapNameSimulator.get(name);
		} else if (classes.containsKey(name)){
			ISimulatorModel instance;
			try {
				instance = classes.get(name).newInstance();
			} catch (Exception e) {
				throw new SimulatorException("Invalid simulator model "+name,e);
			}
			SimulatorModelWrapper wapper = new SimulatorModelWrapper(instance);
			mapNameSimulator.put(name, wapper);
			return wapper;
		} else {
			throw new SimulatorException("Unkown simulator model "+name);
		}
	}
}
