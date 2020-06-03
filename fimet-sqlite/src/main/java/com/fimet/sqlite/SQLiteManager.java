package com.fimet.sqlite;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.ISQLiteManager;
import com.fimet.Manager;
import com.fimet.xml.DatabaseXml;
import com.fimet.xml.FimetXml;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public final class SQLiteManager implements ISQLiteManager {

	private Map<String,String> paths;
	private Map<String,ConnectionSource> connections;

	public SQLiteManager() {
		paths = new HashMap<String,String>();
		connections = new HashMap<String,ConnectionSource>();
		loadDataBases();
	}
	@Override
	public void start() {}
	public ConnectionSource getOrCreateConnection(String path) {
		if (connections.containsKey(path)) {
			return connections.get(path);
		} else {
			try {
				File file = new File(path);
				ConnectionSource connection;
				if (!file.exists()) {
					file.createNewFile();
				}
				connection = newConnection(path);
				connections.put(path, connection);
				return connection;
			} catch (Exception e) {
				throw new FimetException("Error creating database "+path, e);	
			}
		}
	}
	public ConnectionSource newPoolConnection(String id) {
		return null;
	}
	public ConnectionSource getConnection(String id) {
		if (connections.containsKey(id)) {
			return connections.get(id);
		} else if (paths.containsKey(id)) {
			ConnectionSource connection = newConnection(id);
			connections.put(id, connection);
			return connection;
		} else {
			throw new FimetException("Unknow connection "+id);
		}
	}
    public ConnectionSource getFimetConnection() {
    	return getConnection(DB_FIMET);
	}
    private ConnectionSource newConnection(String id) {
    	try {
    		return new JdbcConnectionSource("jdbc:sqlite:"+paths.get(id));
    	} catch (Exception e) {
    		throw new FimetException("Cannot connect to database "+id,e);
    	}
    }
	public void connect(String id) {
		if (!connections.containsKey(id)) {
			if (paths.containsKey(id)) {
				ConnectionSource connection = newConnection(id);
				connections.put(id, connection);
			} else {
				throw new FimetException("Unknow connection "+id);	
			}
		}
	}
	public void disconnect(String id) {
		if (connections.containsKey(id)) {
			ConnectionSource connection = connections.remove(id);
			try {
				connection.close();
			} catch (IOException e) {
				throw new FimetException("Cannot close connection "+id);
			}
		}
	}
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (connections != null && !connections.isEmpty()) {
			for (Map.Entry<String, ConnectionSource> e : connections.entrySet()) {
				s.append(e.getKey()).append(",");
			}
		}
		return s.toString();
	}
	@SuppressWarnings("unchecked")
	private void loadDataBases() {
		FimetXml model = Manager.getModel();
		if (model != null) {
			List<DatabaseXml> databases = model.getDatabases();
			for (DatabaseXml db : databases) {
				try {
					File fileDataBase = new File(db.getPath());
			    	if (!fileDataBase.exists()) {
			    		File parentFile = fileDataBase.getAbsoluteFile().getParentFile();
			    		if (!parentFile.exists()) {
			    			parentFile.mkdirs();
			    		}
						fileDataBase.createNewFile();
						ConnectionSource connection = new JdbcConnectionSource("jdbc:sqlite:"+db.getPath());
						Class<IDataBaseCreator> classCreator = (Class<IDataBaseCreator>)Class.forName(db.getCreator());
						IDataBaseCreator newInstance = classCreator.newInstance();
						newInstance.create(connection);
						connection.close();
			    	}
			    	paths.put(db.getId(), db.getPath());
				} catch (Exception e) {
					FimetLogger.error(SQLiteManager.class,"Error initializing database "+db.getPath());
					throw new FimetException("Error initializing database "+db.getPath(), e);
				}
			}
		}
	}
	@Override
	public void saveState() {}
	@Override
	public void reload() {}
}
