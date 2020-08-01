package com.fimet;

import java.util.List;

import com.fimet.IManager;
import com.fimet.enviroment.IEEnviroment;
import com.fimet.enviroment.IEnviroment;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IEnviromentManager extends IManager {
	public IEnviroment getActive();
	public void connect(IEnviroment enviroment);
	public void disconnect(IEnviroment enviroment);
	public List<IEnviroment> getEnviroments();
	public IEnviroment getEnviroment(String name);
	public IEnviroment getEnviroment(IEEnviroment entity);
}
