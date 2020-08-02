package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorMessageXml;
import com.fimet.simulator.ESimulatorModelXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorModelsXml;

public class SimulatorMessageXmlDAO implements ISimulatorMessageDAO<ESimulatorMessageXml> {
	private File file;
	public SimulatorMessageXmlDAO() {
		String path = Manager.getProperty("simulatorModels.path","fimet/model/simulatorModels.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulatorModels.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public List<ESimulatorMessageXml> findByModelName(String modelName) {
		if (file.exists()) {
			ESimulatorModelXml model = findModel(modelName);
			if (model != null) {
				if (model.getMessages() != null) {
					return model.getMessages();
				} else if (model.getPath() != null) {
					model = XmlUtils.fromPath(model.getPath(), ESimulatorModelXml.class);
					return model.getMessages();
				}
			}
		}
		return null;
	}
	public ESimulatorModelXml findModel(String modelName) {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModelXml> models = simulatorModelsXml.getSimulatorModels();
			if (models != null) {
				for (ESimulatorModelXml m : models) {
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
	public ESimulatorMessageXml insert(ESimulatorMessageXml simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESimulatorMessageXml update(ESimulatorMessageXml simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESimulatorMessageXml delete(ESimulatorMessageXml simulator) {
		throw new RuntimeException("Not yet supported");
	}

}
