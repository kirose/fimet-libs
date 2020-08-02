package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.net.ESocketXml;


@XmlRootElement(name="sockets")
@XmlAccessorType(XmlAccessType.NONE)
public class SocketsXml {
	@XmlElement(name="sockets")
	private List<ESocketXml> sockets;
	public SocketsXml() {
		super();
	}
	public SocketsXml(List<ESocketXml> sockets) {
		super();
		this.sockets = sockets;
	}
	public List<ESocketXml> getSockets() {
		return sockets;
	}
	public void setSockets(List<ESocketXml> sockets) {
		this.sockets = sockets;
	}
}
