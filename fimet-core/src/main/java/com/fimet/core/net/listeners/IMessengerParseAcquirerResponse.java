package com.fimet.core.net.listeners;

import com.fimet.core.iso8583.parser.Message;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerParseAcquirerResponse extends IMessengerListener {
	/**
	 * Invoked after parse the acquirer message
	 * @param response
	 */
	void onMessengerParseAcquirerResponse(Message response);
}
