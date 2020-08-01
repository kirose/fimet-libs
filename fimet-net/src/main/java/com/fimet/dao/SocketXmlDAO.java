package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.socket.ESocket;
import com.fimet.socket.IESocket;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.SocketsXml;

public class SocketXmlDAO  implements ISocketDAO {
	private File file;
	public SocketXmlDAO() {
		String path = Manager.getProperty("sockets.path","fimet/model/sockets.xml");
		if (path == null) {
			throw new PersistenceException("Must declare sockets.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public IESocket findByName(String name) {
		if (file.exists()) {
			SocketsXml socketsXml = XmlUtils.fromFile(file, SocketsXml.class);
			List<ESocket> sockets = socketsXml.getSockets();
			for (ESocket s : sockets) {
				if (name.equals(s.getName())) {
					return validate(s);
				}
			}
		}
		return null;
	}
	@Override
	public List<IESocket> findAll() {
		if (file.exists()) {
			SocketsXml socketsXml = XmlUtils.fromFile(file, SocketsXml.class);
			List<ESocket> sockets = validate(socketsXml.getSockets());
			return CollectionUtils.cast(sockets, IESocket.class);
		}
		return null;
	}
	private List<ESocket> validate(List<ESocket> sockets) {
		if (sockets != null) {
			for (ESocket s : sockets) {
				validate(s);
			}
		}
		return sockets;
	}
	private ESocket validate(ESocket s) {
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
	public IESocket insert(IESocket socket) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESocket update(IESocket socket) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IESocket delete(IESocket socket) {
		throw new RuntimeException("Not yet supported");
	}
}
