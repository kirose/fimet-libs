package com.fimet.core.net.listeners;

import com.fimet.core.iso8583.parser.Message;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerParseIssuerRequest extends IMessengerListener {
	/**
	 * Invoked after the message is parsed
	 * @param request
	 */
	void onMessengerParseIssuerRequest(Message request);
}
