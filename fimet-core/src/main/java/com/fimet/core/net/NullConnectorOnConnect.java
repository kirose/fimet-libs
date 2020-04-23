package com.fimet.core.net;

import com.fimet.core.net.Connector.IConnectorOnConnect;

public class NullConnectorOnConnect implements IConnectorOnConnect {
	public static final IConnectorOnConnect INSTANCE = new NullConnectorOnConnect();
	@Override
	public void onConnectorConnect(IMessenger messenger) {
		
	}

}
