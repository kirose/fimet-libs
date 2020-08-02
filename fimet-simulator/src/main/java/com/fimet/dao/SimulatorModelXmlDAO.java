package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorModelXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorModelsXml;

public class SimulatorModelXmlDAO implements ISimulatorModelDAO<ESimulatorModelXml> {
	private File file;
	public SimulatorModelXmlDAO() {
		String path = Manager.getProperty("simulatorModels.path","fimet/model/simulatorModels.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulatorModels.path property in fimet.xml");
		}
		file = new File(path);
	}

	@Override
	public ESimulatorModelXml findByName(String name) {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModelXml> models = simulatorModelsXml.getSimulatorModels();
			for (ESimulatorModelXml m : models) {
				if (m.getName().equals(name)) {
					return m;
				}
			}
		}
		return null;
	}

	@Override
	public List<ESimulatorModelXml> findAll() {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModelXml> models = simulatorModelsXml.getSimulatorModels();
			return models;
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
	public ESimulatorModelXml insert(ESimulatorModelXml simulator) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public ESimulatorModelXml update(ESimulatorModelXml simulator) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public ESimulatorModelXml delete(ESimulatorModelXml simulator) {
		throw new RuntimeException("Not yet supported");
	}

}
