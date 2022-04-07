package com.fimet.dao;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulatorXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorsXml;
@Component
public class SimulatorXmlDAO implements ISimulatorDAO<ESimulatorXml> {
	private File file;
	public SimulatorXmlDAO() {
	}
	@Override
	public ESimulatorXml findByName(String name) {
		if (file.exists()) {
			SimulatorsXml simulatorsXml = XmlUtils.fromFile(file, SimulatorsXml.class);
			List<ESimulatorXml> simulators = simulatorsXml.getSimulators();
			for (ESimulatorXml s : simulators) {
				if (name.equals(s.getName())) {
					return validate(s);
				}
			}
		}
		return null;
	}
	@Override
	public List<ESimulatorXml> findAll() {
		if (file.exists()) {
			SimulatorsXml simulatorsXml = XmlUtils.fromFile(file, SimulatorsXml.class);
			List<ESimulatorXml> simulators = validate(simulatorsXml.getSimulators());
			return simulators;
		}
		return null;
	}
	private List<ESimulatorXml> validate(List<ESimulatorXml> simulators) {
		if (simulators != null) {
			for (ESimulatorXml s : simulators) {
				validate(s);
			}
		}
		return simulators;
	}
	private ESimulatorXml validate(ESimulatorXml s) {
		if (s.getName() == null) 
			throw new FimetException("name is null for simulator "+s+" in xml");
		if (s.getModel() == null)
			throw new FimetException("model is null for simulator "+s.getName()+" in xml");
		if (s.getParser() == null)
			throw new FimetException("parser is null for simulator "+s.getName()+" in xml");
		if (s.getAdapter() != null||s.getAddress() != null||s.getPort() != null||s.isServer() != null) {
			if (s.getAdapter() == null)
				throw new FimetException("adapter is null for simulator "+s.getName()+" in xml");
			if (s.getPort() == null)
				throw new FimetException("port is null for simulator "+s.getName()+" in xml");
			if (s.getAddress() == null)
				throw new FimetException("address is null for simulator "+s.getName()+" in xml");
			if (s.isServer() == null)
				throw new FimetException("server is null for simulator "+s.getName()+" in xml");
		}
		return s;
	}
	@PostConstruct
	@Override
	public void start() {
		String path = Manager.getProperty("simulators.path","model/simulators.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulators.path property in fimet.xml");
		}
		file = new File(path).getAbsoluteFile();
	}
	@Override
	public void reload() {
		
	}
	@Override
	public ESimulatorXml insert(ESimulatorXml simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESimulatorXml update(ESimulatorXml simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESimulatorXml delete(ESimulatorXml simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
