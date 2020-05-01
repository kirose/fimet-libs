package com.fimet.usecase;

import java.util.List;

import com.fimet.iso8583.parser.Message;
import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IUseCase extends IMultiConnectable {

	public String getName();
	public String getAuthorization();
	public Message getMessage();
	public ISimulator getAcquirer();
	public List<ISimulator> getSimulators();
	public ISimulatorExtension getSimulatorExtension();
}
