package com.fimet.net;

import com.fimet.net.AdaptedSocket.State;
import com.fimet.parser.IStreamAdapter;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocket extends IConnectable {
	void write(byte[] message);
	void write(byte[] message, boolean adapt);
	void setAutoReconnect(boolean reconnect);
	IStreamAdapter getAdapter();
	long getNumOfRead();
	long getNumOfWrite();
	Integer getPort();
	String getAddress();
	boolean isServer();
	String getName();
	void setListener(ISocketListener listener);
	public int getNumOfConnections();
	State getState();
}
