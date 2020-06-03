package com.fimet.socket;

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
	PSocket getParameters();
	long getNumOfRead();
	long getNumOfWrite();
	Integer getPort();
	String getAddress();
	boolean server();
}
