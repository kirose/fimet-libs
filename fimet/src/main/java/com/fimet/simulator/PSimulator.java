package com.fimet.simulator;


import com.fimet.IAdapterManager;
import com.fimet.Manager;
import com.fimet.parser.IAdapter;
import com.fimet.socket.PSocket;
/**
 * Parameter Object for Simulator
 * @author Marco Antonio
 *
 */
public class PSimulator {
	private String parser;
	private String externalId;
	private String model;
	private Integer idSimulator;
	private PSocket pSocket;
	public PSimulator(String externalId, String model, String parser, String address, Integer port, Boolean server, String adapter) {
		this(model, parser, address, port, server, Manager.get(IAdapterManager.class).getAdapter(adapter));
		this.externalId = externalId;
	}

	public PSimulator(String model, String parser, String address, Integer port, Boolean server, String adapter) {
		this(model, parser, address, port, server, adapter != null ? Manager.get(IAdapterManager.class).getAdapter(adapter) : null);
	}
	public PSimulator(String model, String parser, String address, Integer port, Boolean server, IAdapter adapter) {
		this.pSocket = address != null && port != null && server != null && adapter != null ? new PSocket(address, port, server, adapter) : null;
		this.parser = parser;
		this.model = model;		
	}
	public PSimulator(String model, String parser, PSocket socket) {
		this.pSocket = new PSocket(socket.getAddress(), socket.getPort(), socket.isServer(), socket.getAdapter());
		this.parser = parser;
		this.model = model;
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getParser() {
		return parser;
	}
	public void setParser(String parser) {
		this.parser = parser;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getIdSimulator() {
		return idSimulator;
	}
	public void setIdSimulator(Integer idSimulator) {
		this.idSimulator = idSimulator;
	}
	public PSocket getPSocket() {
		return pSocket;
	}
	public void setPSocket(PSocket pSocket) {
		this.pSocket = pSocket;
	}
	public String getAddress() {
		return pSocket.getAddress();
	}
	public Integer getPort() {
		return pSocket.getPort();
	}
	public boolean server() {
		return pSocket.isServer();
	}
	public IAdapter getAdapter() {
		return pSocket.getAdapter();
	}
	public String toString() {
		return externalId != null ? externalId : model;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((parser == null) ? 0 : parser.hashCode());
		result = prime * result + ((pSocket == null) ? 0 : pSocket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PSimulator other = (PSimulator) obj;
		if (idSimulator == null) {
			if (other.idSimulator != null)
				return false;
		} else if (!idSimulator.equals(other.idSimulator))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (pSocket == null) {
			if (other.pSocket != null)
				return false;
		} else if (!pSocket.equals(other.pSocket))
			return false;
		if (parser == null) {
			if (other.parser != null)
				return false;
		} else if (!parser.equals(other.parser))
			return false;
		return true;
	}
}
