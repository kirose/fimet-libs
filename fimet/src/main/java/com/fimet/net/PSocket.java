package com.fimet.net;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.adapter.IAdapter;
/**
 * Parameter Object for AdaptedSocket
 * @author Marco Antonio
 *
 */
public class PSocket {
	static IParserManager parserManager = Manager.get(IParserManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	private String address;
	private Integer port;
	private boolean server;
	private IAdapter adapter;

	public PSocket(String address, Integer port, boolean server, String adapter) {
		this(address, port, server, adapterManager.getAdapter(adapter));
	}
	public PSocket(String address, Integer port, boolean server, IAdapter adapter) {
		super();
		this.address = address;
		this.port = port;
		this.server = server;
		this.adapter = adapter;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public boolean isServer() {
		return server;
	}
	public void setIsServer(boolean server) {
		this.server = server;
	}
	public IAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(IAdapter adapter) {
		this.adapter = adapter;
	}
	public String toString() {
		return (server?"Server":"Client")+" "+address+" "+port;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (server ? 1231 : 1237);
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		PSocket other = (PSocket) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (server != other.server)
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}
}
