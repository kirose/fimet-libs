package com.fimet.core.simulator;

import com.fimet.core.iso8583.parser.Message;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulator {
	public Integer getId();
	public String getName();
	public Message simulateRequest(Message message);
	public Message simulateResponse(Message message);
	public void free();
}
