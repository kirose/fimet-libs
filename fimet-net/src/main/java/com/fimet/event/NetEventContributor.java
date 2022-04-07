package com.fimet.event;

import java.util.List;

import com.fimet.FimetException;
import com.fimet.net.IESocket;
import com.fimet.net.ISocket;

public class NetEventContributor implements IEventContributor {

	@Override
	public Object[] getEventTypes() {
		return NetEvent.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fireEvent(IEvent event, IEventListener listener) {
		NetEvent type = (NetEvent)event.getType();
		switch (type) {
		case SOCKET_INSERTED:
			((ISocketInserted)listener).onSocketInserted((IESocket)event.getParams()[0]);
			break;
		case SOCKET_UPDATED:
			((ISocketUpdated)listener).onSocketUpdated((IESocket)event.getParams()[0]);
			break;
		case SOCKET_DELETED:
			((ISocketDeleted)listener).onSocketDeleted((IESocket)event.getParams()[0]);
			break;
		case SOCKET_LOADED:
			((ISocketLoaded)listener).onSocketLoaded((ISocket)event.getParams()[0]);
			break;
		case SOCKET_REMOVED:
			((ISocketRemoved)listener).onSocketRemoved((ISocket)event.getParams()[0]);
			break;
		case SOCKET_CONNECTED:
			((ISocketConnected)listener).onSocketConnected((ISocket)event.getParams()[0]);
			break;
		case SOCKET_CONNECTING:
			((ISocketConnecting)listener).onSocketConnecting((ISocket)event.getParams()[0]);
			break;
		case SOCKET_DISCONNECTED:
			((ISocketDisconnected)listener).onSocketDisconnected((ISocket)event.getParams()[0]);
			break;
		case SOCKET_MANAGER_RELOADED:
			((ISocketManagerReloaded)listener).onSocketManagerReloaded((List<ISocket>)event.getParams()[0]);
			break;
		default:
			throw new FimetException("Invalid Event "+event);
		}
	}

}
