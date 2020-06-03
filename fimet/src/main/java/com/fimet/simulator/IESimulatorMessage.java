package com.fimet.simulator;

import java.util.List;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IESimulatorMessage {
	public static String REQUEST = "Request";
	public static String RESPONSE = "Response";
	public String getHeader();
	public String getMti();
	public String getType();
	public List<String> getDelFields();
	public List<SimulatorField> getAddFields();
}
