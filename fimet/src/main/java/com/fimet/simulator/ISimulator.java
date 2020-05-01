package com.fimet.simulator;

import com.fimet.iso8583.parser.IParser;
import com.fimet.iso8583.parser.Message;
import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulator extends IConnectable {
	public ISimulatorModel getModel();
	public IParser getParser();
	public ISocket getSocket();
	public void setListener(ISimulatorListener listener);
	public void free();
	public Message simulateRequest(Message message);
	public Message simulateResponse(Message message);
	public void writeMessage(Message message);
	public void doWriteMessage(Message message);
	public void doReadMessage(byte[] bytes);
}
