package com.fimet.usecase;

import java.util.List;

import com.fimet.exe.UseCaseResult;
import com.fimet.net.IMultiConnectable;
import com.fimet.parser.IMessage;
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
	public IMessage getMessage();
	public ISimulator getAcquirer();
	public List<ISimulator> getSimulators();
	public ISimulatorExtension getSimulatorExtension();
	public UseCaseResult getResult();
	public IMessage getResponse();
	public Object getSource();
}
