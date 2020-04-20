package com.fimet.core.validator;


import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IValidator {
	
	/**
	 * This method will be invoked on the issuer request 
	 * @param request message
	 */
	public void onWriteMessage(ISocket socket, Message msg);
	/**
	 * This method will be invoked 
	 * @param request message
	 */
	public void onReadMessage(ISocket socket, Message msg);
}
