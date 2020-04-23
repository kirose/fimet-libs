package com.fimet.core.usecase;

import java.util.List;

import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.ISocket;
import com.fimet.core.net.Connector.IConnectable;
import com.fimet.core.validator.IValidator;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IUseCase extends IConnectable {

	public String getName();
	public String getAuthorization();
	public Message getMessage();
	public ISocket getAcquirer();
	public List<ISocket> getConnections();
	public int getMaxExecutionTime();
	public IValidator getValidator();
}
