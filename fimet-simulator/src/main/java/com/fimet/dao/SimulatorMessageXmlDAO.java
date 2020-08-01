package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorModel;
import com.fimet.simulator.IESimulatorMessage;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorModelsXml;

public class SimulatorMessageXmlDAO implements ISimulatorMessageDAO {
	private File file;
	public SimulatorMessageXmlDAO() {
		String path = Manager.getProperty("simulatorModels.path","fimet/model/simulatorModels.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulatorModels.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public List<IESimulatorMessage> findByModelName(String modelName) {
		if (file.exists()) {
			ESimulatorModel model = findModel(modelName);
			if (model != null) {
				if (model.getMessages() != null) {
					return CollectionUtils.cast(model.getMessages(), IESimulatorMessage.class);
				} else if (model.getPath() != null) {
					model = XmlUtils.fromPath(model.getPath(), ESimulatorModel.class);
					return CollectionUtils.cast(model.getMessages(), IESimulatorMessage.class);
				}
			}
		}
		return null;
	}
	public ESimulatorModel findModel(String modelName) {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModel> models = simulatorModelsXml.getSimulatorModels();
			if (models != null) {
				for (ESimulatorModel m : models) {
					if (m.getName().equals(modelName)) {
						return m;
					}
				}
			}
		}
		return null;
	}
	@Override
	public void start() {
		
	}
	@Override
	public void reload() {
		
	}
	@Override
	public IESimulatorMessage insert(IESimulatorMessage simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESimulatorMessage update(IESimulatorMessage simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESimulatorMessage delete(IESimulatorMessage simulator) {
		throw new RuntimeException("Not yet supported");
	}

}
