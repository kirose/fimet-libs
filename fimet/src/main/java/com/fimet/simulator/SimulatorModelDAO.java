package com.fimet.simulator;

import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.ISimulatorModelDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.xml.FimetXml;

public class SimulatorModelDAO implements ISimulatorModelDAO {

	public SimulatorModelDAO() {
	}

	@Override
	public IESimulatorModel getByName(String name) {
		FimetXml model = Manager.getModel();
		List<ESimulatorModel> models = model.getSimulatorModels();
		for (ESimulatorModel m : models) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	@Override
	public List<IESimulatorModel> getAll() {
		FimetXml model = Manager.getModel();
		return CollectionUtils.cast(model.getSimulatorModels(), IESimulatorModel.class);
	}

}
