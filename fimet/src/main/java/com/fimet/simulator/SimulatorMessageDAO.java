package com.fimet.simulator;

import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.ISimulatorMessageDAO;
import com.fimet.simulator.IESimulatorMessage;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.FimetXml;

public class SimulatorMessageDAO implements ISimulatorMessageDAO {

	@Override
	public List<IESimulatorMessage> getByModelName(String modelName) {
		ESimulatorModel model = getModel(modelName);
		if (model != null) {
			if (model.getMessages() != null) {
				return CollectionUtils.cast(model.getMessages(), IESimulatorMessage.class);
			} else if (model.getPath() != null) {
				model = XmlUtils.fromPath(model.getPath(), ESimulatorModel.class);
				return CollectionUtils.cast(model.getMessages(), IESimulatorMessage.class);
			}
		}
		return null;
	}
	public ESimulatorModel getModel(String modelName) {
		FimetXml model = Manager.getModel();
		List<ESimulatorModel> models = model.getSimulatorModels();
		if (models != null) {
			for (ESimulatorModel m : models) {
				if (m.getName().equals(modelName)) {
					return m;
				}
			}
		}
		return null;
	}

}
