package com.fimet.simulator;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IESimulator {
	public String getName();
	public void setName(String name);
	public String getParser();
	public void setParser(String parser);
	public String getModel();
	public void setModel(String model);
	public String getAddress();
	public void setAddress(String address);
	public Integer getPort();
	public void setPort(Integer port);
	public boolean isServer();
	public void setServer(boolean server);
	public String getAdapter();
	public void setAdapter(String adapter);
}
