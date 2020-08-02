package com.fimet.event;

import java.util.List;

import com.fimet.FimetException;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.IESimulatorModel;
import com.fimet.simulator.ISimulator;

public class SimulatorEventContributor implements IEventContributor {

	@Override
	public Object[] getEventTypes() {
		return SimulatorEvent.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fireEvent(IEvent event, IEventListener listener) {
		SimulatorEvent type = (SimulatorEvent)event.getType();
		switch (type) {
		case SIMULATOR_INSERTED:
			((ISimulatorInserted)listener).onSimulatorInserted((IESimulator)event.getParams()[0]);
			break;
		case SIMULATOR_UPDATED:
			((ISimulatorUpdated)listener).onSimulatorUpdated((IESimulator)event.getParams()[0]);
			break;
		case SIMULATOR_DELETED:
			((ISimulatorDeleted)listener).onSimulatorDeleted((IESimulator)event.getParams()[0]);
			break;
		case SIMULATOR_LOADED:
			((ISimulatorLoaded)listener).onSimulatorLoaded((ISimulator)event.getParams()[0]);
			break;
		case SIMULATOR_REMOVED:
			((ISimulatorRemoved)listener).onSimulatorRemoved((ISimulator)event.getParams()[0]);
			break;
		case SIMULATOR_CONNECTED:
			((ISimulatorConnected)listener).onSimulatorConnected((ISimulator)event.getParams()[0]);
			break;
		case SIMULATOR_CONNECTING:
			((ISimulatorConnecting)listener).onSimulatorConnecting((ISimulator)event.getParams()[0]);
			break;
		case SIMULATOR_DISCONNECTED:
			((ISimulatorDisconnected)listener).onSimulatorDisconnected((ISimulator)event.getParams()[0]);
			break;
		case SIMULATOR_MODEL_INSERTED:
			((ISimulatorModelInserted)listener).onSimulatorModelInserted((IESimulatorModel)event.getParams()[0]);
			break;
		case SIMULATOR_MODEL_UPDATED:
			((ISimulatorModelUpdated)listener).onSimulatorModelUpdated((IESimulatorModel)event.getParams()[0]);
			break;
		case SIMULATOR_MODEL_DELETED:
			((ISimulatorModelDeleted)listener).onSimulatorModelDeleted((IESimulatorModel)event.getParams()[0]);
			break;
		case SIMULATOR_MANAGER_RELOADED:
			((ISimulatorManagerReloaded)listener).onSimulatorManagerReloaded((List<ISimulator>)event.getParams()[0]);
			break;
		default:
			throw new FimetException("Invalid Event "+event);
		}
	}

}
