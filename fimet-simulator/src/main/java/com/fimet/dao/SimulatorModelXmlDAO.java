package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorModel;
import com.fimet.simulator.IESimulatorModel;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorModelsXml;

public class SimulatorModelXmlDAO implements ISimulatorModelDAO {
	private File file;
	public SimulatorModelXmlDAO() {
		String path = Manager.getProperty("simulatorModels.path","fimet/model/simulatorModels.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulatorModels.path property in fimet.xml");
		}
		file = new File(path);
	}

	@Override
	public IESimulatorModel findByName(String name) {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModel> models = simulatorModelsXml.getSimulatorModels();
			for (ESimulatorModel m : models) {
				if (m.getName().equals(name)) {
					return m;
				}
			}
		}
		return null;
	}

	@Override
	public List<IESimulatorModel> findAll() {
		if (file.exists()) {
			SimulatorModelsXml simulatorModelsXml = XmlUtils.fromFile(file, SimulatorModelsXml.class);
			List<ESimulatorModel> models = simulatorModelsXml.getSimulatorModels();
			return CollectionUtils.cast(models, IESimulatorModel.class);
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
	public IESimulatorModel insert(IESimulatorModel simulator) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public IESimulatorModel update(IESimulatorModel simulator) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public IESimulatorModel delete(IESimulatorModel simulator) {
		throw new RuntimeException("Not yet supported");
	}

}
