package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.socket.ESocket;


@XmlRootElement(name="sockets")
@XmlAccessorType(XmlAccessType.NONE)
public class SocketsXml {
	@XmlElement(name="sockets")
	private List<ESocket> sockets;
	public SocketsXml() {
		super();
	}
	public SocketsXml(List<ESocket> sockets) {
		super();
		this.sockets = sockets;
	}
	public List<ESocket> getSockets() {
		return sockets;
	}
	public void setSockets(List<ESocket> sockets) {
		this.sockets = sockets;
	}
}
