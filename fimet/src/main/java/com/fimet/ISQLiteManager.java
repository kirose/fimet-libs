package com.fimet;

import com.j256.ormlite.support.ConnectionSource;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISQLiteManager extends IManager {
	public static final String DB_FIMET = "fimet";
	public ConnectionSource getConnection(String path);
	public ConnectionSource getFimetConnection();
	public void connect(String path);
	public void disconnect(String path);
}
