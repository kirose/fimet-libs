package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.net.ESocketXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SocketsXml;

public class SocketXmlDAO implements ISocketDAO<ESocketXml> {
	private File file;
	public SocketXmlDAO() {
		String path = Manager.getProperty("sockets.path","fimet/model/sockets.xml");
		if (path == null) {
			throw new PersistenceException("Must declare sockets.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public ESocketXml findByName(String name) {
		if (file.exists()) {
			SocketsXml socketsXml = XmlUtils.fromFile(file, SocketsXml.class);
			List<ESocketXml> sockets = socketsXml.getSockets();
			for (ESocketXml s : sockets) {
				if (name.equals(s.getName())) {
					return validate(s);
				}
			}
		}
		return null;
	}
	@Override
	public List<ESocketXml> findAll() {
		if (file.exists()) {
			SocketsXml socketsXml = XmlUtils.fromFile(file, SocketsXml.class);
			List<ESocketXml> sockets = validate(socketsXml.getSockets());
			return sockets;
		}
		return null;
	}
	private List<ESocketXml> validate(List<ESocketXml> sockets) {
		if (sockets != null) {
			for (ESocketXml s : sockets) {
				validate(s);
			}
		}
		return sockets;
	}
	private ESocketXml validate(ESocketXml s) {
		if (s.getAdapter() == null)
			throw new FimetException("adapter is null for socket "+s.getName()+" in xml");
		if (s.getPort() == null)
			throw new FimetException("port is null for socket "+s.getName()+" in xml");
		if (s.getAddress() == null)
			throw new FimetException("address is null for socket "+s.getName()+" in xml");
		if (s.isServer() == null)
			throw new FimetException("server is null for socket "+s.getName()+" in xml");
		return s;
	}
	@Override
	public void start() {
		
	}
	@Override
	public void reload() {
		
	}
	@Override
	public ESocketXml insert(ESocketXml socket) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESocketXml update(ESocketXml socket) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public ESocketXml delete(ESocketXml socket) {
		throw new RuntimeException("Not yet supported");
	}
}
