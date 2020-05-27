package com.fimet.simulator;

import com.fimet.parser.IMessage;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorModel {
	public String getName();
	public IMessage simulateRequest(IMessage message);
	public IMessage simulateResponse(IMessage message);
}
