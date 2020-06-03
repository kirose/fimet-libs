package com.fimet.simulator;

import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.ISimulatorDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.xml.FimetXml;

public class SimulatorDAO implements ISimulatorDAO {
	public SimulatorDAO() {}
	@Override
	public IESimulator getByName(String name) {
		FimetXml model = Manager.getModel();
		List<ESimulator> simulators = model.getSimulators();
		for (ESimulator s : simulators) {
			if (name.equals(s.getName())) {
				return validate(s);
			}
		}
		return null;
	}
	@Override
	public List<IESimulator> getAll() {
		FimetXml model = Manager.getModel();
		List<ESimulator> simulators = validate(model.getSimulators());
		return CollectionUtils.cast(simulators, IESimulator.class);
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
		if (s.getAdapter() == null)
			throw new FimetException("adapter is null for simulator "+s.getName()+" in xml");
		if (s.getPort() == null)
			throw new FimetException("port is null for simulator "+s.getName()+" in xml");
		if (s.getModel() == null)
			throw new FimetException("model is null for simulator "+s.getName()+" in xml");
		if (s.getParser() == null)
			throw new FimetException("parser is null for simulator "+s.getName()+" in xml");
		if (s.getAddress() == null)
			throw new FimetException("address is null for simulator "+s.getName()+" in xml");
		return s;
	}
}
