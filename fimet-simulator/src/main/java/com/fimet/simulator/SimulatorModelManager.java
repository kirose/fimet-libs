package com.fimet.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.ISimulatorModelManager;
import com.fimet.entity.sqlite.ESimulator;
import com.fimet.entity.sqlite.ESimulatorMessage;
import com.fimet.persistence.dao.SimulatorDAO;
import com.fimet.persistence.dao.SimulatorMessageDAO;
import com.fimet.simulator.ISimulatorModel;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class SimulatorModelManager implements ISimulatorModelManager {
	private Map<Integer, ISimulatorModel> models = new HashMap<>();
	public SimulatorModelManager() {
	}
	
	public List<ESimulator> getEntities(){
		return SimulatorDAO.getInstance().findAll();
	}
	public  List<ESimulatorMessage> getEntityMessages(int id) {
		return SimulatorMessageDAO.getInstance().findByIdSmulator(id);
	}
	public void freeSimulator(int id) {
		if (models.containsKey(id)) {
			models.get(id).free();
		}
	}
	@Override
	public void free() {
		for (java.util.Map.Entry<Integer, ISimulatorModel> e : models.entrySet()) {
			e.getValue().free();
		}
	}
	public ESimulator getEntity(String name) {
		return SimulatorDAO.getInstance().findByName(name);
	}
	@Override
	public void saveState() {}
	@Override
	public void freeSimulators(List<Integer> ids) {
		for (Integer id : ids) {
			freeSimulator(id);
		}
	}
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
		ESimulator entity = getEntity(name);
		if (entity != null) {
			models.put(entity.getId(), new SimulatorModel(entity.getId(), entity.getName()));
			return models.get(entity.getId());
		} else {
			return NullSimulatorModel.INSTANCE;
		}
	}
	public ISimulatorModel getSimulatorModel(Integer idSimulator) {
		if (models.containsKey(idSimulator)) {
			return models.get(idSimulator);
		} else {
			ESimulator entity = getEntity(idSimulator);
			if (entity != null) {
				models.put(entity.getId(), new SimulatorModel(entity.getId(), entity.getName()));
				return models.get(idSimulator);
			} else {
				return NullSimulatorModel.INSTANCE;
			}
		}
	}

}
