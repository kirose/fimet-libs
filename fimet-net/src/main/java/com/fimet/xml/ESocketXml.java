package com.fimet.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.net.IESocket;
import com.fimet.parser.IAdapter;
/**
 * Parameter Object for AdaptedSocket
 * @author Marco Antonio
 *
 */
@XmlRootElement(name="socket")
@XmlAccessorType(XmlAccessType.NONE)
public class ESocketXml implements IESocket {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="address")
	private String address;
	@XmlAttribute(name="port")
	private Integer port;
	@XmlAttribute(name="server")
	private Boolean server;
	@XmlAttribute(name="adapter")
	private String adapter;
	@XmlAttribute(name="maxConnections")
	private Integer maxConnections;
	public ESocketXml() {}
	public ESocketXml(String name, String address, Integer port, boolean server, IAdapter adapter) {
		this(name, address, port, server, adapter.getName());
	}
	public ESocketXml(String name, String address, Integer port, boolean server, String adapter) {
		super();
		this.name = name;
		this.address = address;
		this.port = port;
		this.server = server;
		this.adapter = adapter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Boolean isServer() {
		return server;
	}
	public void setIsServer(Boolean server) {
		this.server = server;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public Integer getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}
	public String toString() {
		return (server?"Server":"Client")+" "+address+" "+port;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + (server!=null&&server ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ESocketXml other = (ESocketXml) obj;
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
