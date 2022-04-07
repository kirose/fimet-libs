package com.fimet.dao;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.IPropertiesManager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorModelXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorModelsXml;

@Component
public class SimulatorModelXmlDAO implements ISimulatorModelDAO<ESimulatorModelXml> {
	@Autowired private IPropertiesManager properties;
	private File file;
	public SimulatorModelXmlDAO() {
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
	@PostConstruct
	@Override
	public void start() {
		String path = properties.getString("simulator.models.path","model/simulatorModels.xml");
		if (path == null) {
			throw new PersistenceException("simulator.models.path is a property required in fimet.xml");
		}
		file = new File(path).getAbsoluteFile();
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

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
