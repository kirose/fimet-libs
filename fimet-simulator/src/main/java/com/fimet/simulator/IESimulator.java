package com.fimet.simulator;

import com.fimet.net.IESocket;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IESimulator extends IESocket {
	public String getName();
	public String getParser();
	public String getModel();
	public String getGroup();
	public String getAddress();
}
