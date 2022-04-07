package com.fimet.net;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketListener {
	void onSocketRead(byte[] bytes);
}