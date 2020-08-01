package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.PersistenceException;
import com.fimet.simulator.ESimulator;
import com.fimet.simulator.IESimulator;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SimulatorsXml;

public class SimulatorXmlDAO implements ISimulatorDAO {
	private File file;
	public SimulatorXmlDAO() {
		String path = Manager.getProperty("simulators.path","fimet/model/simulators.xml");
		if (path == null) {
			throw new PersistenceException("Must declare simulators.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public IESimulator findByName(String name) {
		if (file.exists()) {
			SimulatorsXml simulatorsXml = XmlUtils.fromFile(file, SimulatorsXml.class);
			List<ESimulator> simulators = simulatorsXml.getSimulators();
			for (ESimulator s : simulators) {
				if (name.equals(s.getName())) {
					return validate(s);
				}
			}
		}
		return null;
	}
	@Override
	public List<IESimulator> findAll() {
		if (file.exists()) {
			SimulatorsXml simulatorsXml = XmlUtils.fromFile(file, SimulatorsXml.class);
			List<ESimulator> simulators = validate(simulatorsXml.getSimulators());
			return CollectionUtils.cast(simulators, IESimulator.class);
		}
		return null;
	}
	private List<ESimulator> validate(List<ESimulator> simulators) {
		if (simulators != null) {
			for (ESimulator s : simulators) {
				validate(s);
			}
		}
		return simulators;
	}
	private ESimulator validate(ESimulator s) {
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
	@Override
	public void start() {
		
	}
	@Override
	public void reload() {
		
	}
	@Override
	public IESimulator insert(IESimulator simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESimulator update(IESimulator simulator) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESimulator delete(IESimulator simulator) {
		throw new RuntimeException("Not yet supported");
	}
}
