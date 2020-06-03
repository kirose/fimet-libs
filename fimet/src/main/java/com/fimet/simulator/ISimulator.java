package com.fimet.simulator;

import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.socket.IConnectable;
import com.fimet.socket.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulator extends IConnectable {
	public String getName();
	public ISimulatorModel getModel();
	public IParser getParser();
	public ISocket getSocket();
	public void setListener(ISimulatorListener listener);
	public IMessage simulateRequest(IMessage message);
	public IMessage simulateResponse(IMessage message);
	public void writeMessage(IMessage message);
	public void doWriteMessage(IMessage message);
	public void doReadMessage(byte[] bytes);
	public long getNumOfApprovals();
}
