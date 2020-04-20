package com.fimet.core.net.listeners;

import com.fimet.core.net.IMessenger;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerWriteAcquirerRequest extends IMessengerListener {
	/**
	 * Invoked on Acquirer request
	 * Acquirer (request) -> Authentic
	 * Issuer (response) -> Authentic 
	 * @param conn
	 * @param msg
	 */
	void onMessengerWriteAcquirerRequest(IMessenger conn, byte[] message);
}
