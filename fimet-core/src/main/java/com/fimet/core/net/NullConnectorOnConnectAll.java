package com.fimet.core.net;

import com.fimet.core.net.Connector.IConnectable;
import com.fimet.core.net.Connector.IConnectorOnConnectAll;

public class NullConnectorOnConnectAll implements IConnectorOnConnectAll {
	public static final IConnectorOnConnectAll INSTANCE = new NullConnectorOnConnectAll();
	@Override
	public void onConnectorConnectAll(IConnectable connectable) {}

}
