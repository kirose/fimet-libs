package com.fimet.core.net.listeners;

import com.fimet.core.net.IMessenger;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerWriteIssuerResponse extends IMessengerListener {
	/**
	 * Invoked on Issuer response
	 * Acquirer (request) -> Authentic
	 * Issuer (response) -> Authentic 
	 * @param conn
	 * @param msg
	 */
	void onMessengerWriteIssuerResponse(IMessenger conn, byte[] message);
}
