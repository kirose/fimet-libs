package com.fimet.event;

import java.util.List;

import com.fimet.FimetException;
import com.fimet.enviroment.IEEnviroment;
import com.fimet.enviroment.IEnviroment;
import com.fimet.ftp.IEFtp;
import com.fimet.ftp.IFtp;

public class EnviromentEventContributor implements IEventContributor {

	@Override
	public Object[] getEventTypes() {
		return EnviromentEvent.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fireEvent(IEvent event, IEventListener listener) {
		EnviromentEvent type = (EnviromentEvent)event.getType();
		switch (type) {
		case ENVIROMENT_INSERTED:
			((IEnviromentInserted)listener).onEnviromentInserted((IEEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_UPDATED:
			((IEnviromentUpdated)listener).onEnviromentUpdated((IEEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_DELETED:
			((IEnviromentDeleted)listener).onEnviromentDeleted((IEEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_CONNECTED:
			((IEnviromentConnected)listener).onEnviromentConnected((IEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_CONNECTING:
			((IEnviromentConnecting)listener).onEnviromentConnecting((IEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_DISCONNECTED:
			((IEnviromentDisconnected)listener).onEnviromentDisconnected((IEnviroment)event.getParams()[0]);
			break;
		case ENVIROMENT_MANAGER_RELOADED:
			((IEnviromentManagerReloaded)listener).onEnviromentManagerReloaded((List<IEnviroment>)event.getParams()[0]);
			break;
		case FTP_INSERTED:
			((IFtpInserted)listener).onFtpInserted((IEFtp)event.getParams()[0]);
			break;
		case FTP_UPDATED:
			((IFtpUpdated)listener).onFtpUpdated((IEFtp)event.getParams()[0]);
			break;
		case FTP_DELETED:
			((IFtpDeleted)listener).onFtpDeleted((IEFtp)event.getParams()[0]);
			break;
		case FTP_CONNECTED:
			((IFtpConnected)listener).onFtpConnected((IFtp)event.getParams()[0]);
			break;
		case FTP_CONNECTING:
			((IFtpConnecting)listener).onFtpConnecting((IFtp)event.getParams()[0]);
			break;
		case FTP_DISCONNECTED:
			((IFtpDisconnected)listener).onFtpDisconnected((IFtp)event.getParams()[0]);
			break;
		case FTP_REMOVED:
			((IFtpRemoved)listener).onFtpRemoved((IFtp)event.getParams()[0]);
			break;
		default:
			throw new FimetException("Invalid Event "+event);
		}
	}

}
