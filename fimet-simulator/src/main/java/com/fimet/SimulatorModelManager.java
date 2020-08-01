package com.fimet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.IClassLoaderManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.dao.ISimulatorModelDAO;
import com.fimet.dao.SimulatorModelXmlDAO;
import com.fimet.simulator.IESimulatorModel;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.NullSimulatorModel;
import com.fimet.simulator.SimulatorException;
import com.fimet.simulator.SimulatorModelWrapper;
import com.fimet.simulator.field.*;
import com.fimet.utils.SimulatorUtils;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorModelManager implements ISimulatorModelManager {
	private Map<String, SimulatorModelWrapper> mapNameSimulator = new HashMap<>();
	private Map<String, Class<ISimulatorModel>> classes = new HashMap<>();
	private ISimulatorModelDAO dao = Manager.get(ISimulatorModelDAO.class, SimulatorModelXmlDAO.class);
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
		boolean recompileModels = Manager.getPropertyBoolean("simulatorModel.recompileModels",true);
		IClassLoaderManager loaderManager = Manager.get(IClassLoaderManager.class);
		List<IESimulatorModel> entities = dao.findAll();
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
		IESimulatorModel entity = dao.findByName(name);
		if (entity != null) {
			Class<ISimulatorModel> clazz = SimulatorUtils.reinstallClassSimulatorModel(entity);
			classes.put(name, clazz);
			SimulatorModelWrapper wrapper = mapNameSimulator.get(name);
			if (wrapper != null) {
				ISimulatorModel instance;
				try {
					instance = clazz.newInstance();
				} catch (Exception e) {
					throw new SimulatorException("Invalid Simulator Model "+name,e);
				}
				FimetLogger.debug(getClass(),"Reloaded Simulator Model "+name);
				wrapper.setWapped(instance);
			} else {
				FimetLogger.debug(getClass(), "Unkown Simulator Model Wrapper is null for "+name);
			}
		} else if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.remove(name).setWapped(NullSimulatorModel.INSTANCE);
		} else {
			FimetLogger.debug(getClass(), "Unkown Simulator Model Wrapper "+name);
		}
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
	public void start() {
	}
}
