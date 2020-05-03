package com.fimet.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.commons.exception.FimetException;
import com.fimet.commons.exception.SimulatorException;
import com.fimet.entity.sqlite.ESimulator;
import com.fimet.entity.sqlite.ESimulatorMessage;
import com.fimet.persistence.dao.SimulatorDAO;
import com.fimet.persistence.dao.SimulatorMessageDAO;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.utils.SimulatorUtils;
import com.fimet.xml.FimetXml;
import com.fimet.xml.SimulatorModelXml;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorModelManager implements ISimulatorModelManager {
	private Map<String, SimulatorModelWrapper> models = new HashMap<>();
	private Map<String, Class<ISimulatorModel>> classes = new HashMap<>();
	public SimulatorModelManager() {
		loadInternalClases();
		loadExternalClases();
		models.put("None", new SimulatorModelWrapper(NullSimulatorModel.INSTANCE));
	}
	@SuppressWarnings("unchecked")
	private void loadExternalClases() {
		FimetXml model = Manager.getModel();
		if (
			model != null
			&& model.getSimulators() != null
			&& model.getSimulators().getModels() != null
			&& !model.getSimulators().getModels().isEmpty()
		) {
			List<SimulatorModelXml> models = model.getSimulators().getModels();
			for (SimulatorModelXml m : models) {
				try {
					Class<ISimulatorModel> clazz = (Class<ISimulatorModel>)Class.forName(m.getClassName());
					this.classes.put(m.getId(), clazz);
				} catch (ClassNotFoundException e) {
					throw new FimetException("SimulatorModel Class not found "+m.getClassName(),e);
				}
			}
		}
	}
	private void loadInternalClases() {
		List<ESimulator> entities = getEntities();
		Boolean recompile = Manager.getPropertyBoolean("simulator.recompileModelsAtStartup", Boolean.FALSE);
		if (recompile) {
			for (ESimulator s : entities) {
				Class<ISimulatorModel> sm = SimulatorUtils.getClassSimulatorModel(s);
				classes.put(s.getName(), sm);
			}
		} else {
			for (ESimulator s : entities) {
				Class<ISimulatorModel> sm = SimulatorUtils.installClassSimulatorModel(s);
				classes.put(s.getName(), sm);
			}
		}
	} 
	
	public List<ESimulator> getEntities(){
		return SimulatorDAO.getInstance().findAll();
	}
	public  List<ESimulatorMessage> getEntityMessages(int id) {
		return SimulatorMessageDAO.getInstance().findByIdSimulator(id);
	}
	@Override
	public void reloadSimulator(String name) {
		ESimulator entity = getEntity(name);
		if (entity != null) {
			Class<ISimulatorModel> clazz = SimulatorUtils.installClassSimulatorModel(entity);
			classes.put(name, clazz);
			SimulatorModelWrapper wrapper = models.get(name);
			if (wrapper != null) {
				ISimulatorModel instance;
				try {
					instance = classes.get(name).newInstance();
				} catch (Exception e) {
					throw new SimulatorException("Invalid simulator model "+name,e);
				}
				wrapper.setWapped(instance);
			}
		}
	}
	@Override
	public void reloadSimulators(List<String> names) {
		for (String name: names) {
			reloadSimulator(name);
		}
	}
	@Override
	public void free() {
	}
	public ESimulator getEntity(String name) {
		return SimulatorDAO.getInstance().findByName(name);
	}
	@Override
	public void saveState() {}
	public  ESimulator getEntity(Integer id) {
		return SimulatorDAO.getInstance().findById(id);
	}
	public ESimulator saveSimulator(ESimulator simulator) {
		SimulatorDAO.getInstance().insertOrUpdate(simulator);
		if (simulator.getId() == null) {
			ESimulator last = SimulatorDAO.getInstance().findLast();
			if (last != null)
				simulator.setId(last.getId());
		}
		return simulator;
	}
	public ESimulator deleteSimulator(ESimulator simulator) {
		SimulatorDAO.getInstance().delete(simulator);
		SimulatorMessageDAO.getInstance().deleteByIdSimulator(simulator.getId());
		return simulator;
	}
	public ESimulatorMessage saveSimulatorMessage(ESimulatorMessage message) {
		SimulatorMessageDAO.getInstance().insertOrUpdate(message);
		if (message.getId() == null) {
			ESimulatorMessage last = SimulatorMessageDAO.getInstance().findLast();
			if (last != null)
				message.setId(last.getId());
		}
		return message;
	}
	public ESimulatorMessage deleteSimulatorMessage(ESimulatorMessage message) {
		SimulatorMessageDAO.getInstance().delete(message);
		return message;
	}
	public Integer getNextIdSimulator() {
		return SimulatorDAO.getInstance().getNextSequenceId();
	}
	public Integer getPrevIdSimulator() {
		return SimulatorDAO.getInstance().getPrevSequenceId();
	}
	@Override
	public List<ESimulator> getAcquirerEntities() {
		return SimulatorDAO.getInstance().findAllAcquirers();
	}
	@Override
	public List<ESimulator> getIssuerEntities() {
		return SimulatorDAO.getInstance().findAllIssuers();
	}
	public ISimulatorModel getSimulatorModel(String name) {
		if (models.containsKey(name)) {
			return models.get(name);
		} else if (classes.containsKey(name)){
			ISimulatorModel instance;
			try {
				instance = classes.get(name).newInstance();
			} catch (Exception e) {
				throw new SimulatorException("Invalid simulator model "+name,e);
			}
			SimulatorModelWrapper wapper = new SimulatorModelWrapper(instance);
			models.put(name, wapper);
			return wapper;
		} else {
			throw new FimetException("Unkown simulator model "+name);
		}
	}
}
