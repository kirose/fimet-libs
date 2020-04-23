package com.fimet.core.net;

import com.fimet.core.net.ISocket;
import com.fimet.core.simulator.ISimulator;
import com.fimet.core.net.IMessengerListener;
import com.fimet.core.iso8583.parser.Message;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessenger {
	void writeMessage(Message msg);
	void connect();
	void disconnect();
	IAdaptedSocket getSocket();
	ISocket getConnection();
	boolean isConnected();
	boolean isDisconnected();
	boolean isConnecting();
	void setListener(IMessengerListener listener);
	IMessengerListener getListener();
	ISimulator getSimulator();
}
