package com.fimet.simulator;

import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulator extends IConnectable {
	public String getName();
	public String getGroup();
	public ISimulatorModel getModel();
	public IParser getParser();
	public ISocket getSocket();
	public void setListener(ISimulatorListener listener);
	public void writeMessage(IMessage message);
	public void doWriteMessage(IMessage message);
	public void doReadMessage(byte[] bytes);
	public long getNumOfApprovals();
}
