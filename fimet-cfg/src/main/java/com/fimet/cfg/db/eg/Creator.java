package com.fimet.cfg.db.eg;


import java.io.IOException;
import java.sql.SQLException;

import com.fimet.core.Manager;
import com.fimet.core.entity.sqlite.UseCaseReport;
import com.fimet.core.persistence.ISQLiteManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Creator {
	ConnectionSource connection;
	public Creator(ConnectionSource connection) throws SQLException {
		this.connection = connection;
		//connection = new JdbcConnectionSource("jdbc:sqlite:plugins/fimet.db");
	}
	private Creator() throws SQLException {
		//ISQLiteManager sqliteManager = Manager.get(ISQLiteManager.class);
		//connection = sqliteManager.getConnection();
		connection = new JdbcConnectionSource("jdbc:sqlite:plugins/fimet.db");
	}
	public Creator createDataBase() {
		try {
			//ISQLiteManager manager = Manager.get(ISQLiteManager.class);
			//System.out.println(manager);
			//TableUtils.dropTable(connection, UseCaseReport.class, true);
			//TableUtils.dropTable(connection, com.fimet.entity.sqlite.MachinePreference.class, true);
			//TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.MachinePreference.class);
			//TableUtils.dropTable(connection, com.fimet.core.entity.sqlite.TransactionType.class, true);
//			TableUtils.dropTable(connection, com.fimet.core.entity.sqlite.UseCaseReport.class, true);
//			TableUtils.dropTable(connection, com.fimet.core.entity.sqlite.Socket.class, true);
//			TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.UseCaseReport.class);
//			TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Socket.class);
	
			//new PanCreator(connection).create().insertAll();
			//new ParserCreator(connection).drop().create().insertAll();
			new FieldFormatCreator(connection).insertPOSNacional();
			//new FieldFormatCreator(connection).insertGroups();
			//new SourceCreator(connection).drop().create().insertAll();
			//new RulesCreator(connection).drop().create().insertAll();
			//new FieldFormatCreator(connection).insertVisa();
			//new SimulatorCreator(connection).insertPOS34IssStratus();

			
			//new SimulatorCreator().drop().create().insertAll();
			//new RulesCreator().drop().create().insertAll();
			//new FieldFormatCreator().create().insertAll();
			
			//new EnviromentCreator(connection).drop().create().insertAll();
			//new DataBaseCreator().drop().create().insertAll();
			//new ParserCreator().drop().create().insertAll();
//		    .insertPOSNacionalExtractBase()
//		    .insertPOSNacionalExtractAdd1()
//		    .insertPOSNacionalExtractAdd2()
//		    .insertPOSNacionalExtractAdd3()
//		    .insertPOSNacionalExtractAdd4()
//		    .insertPOSNacionalExtractAdd5()
//		    .insertPOSNacionalExtractAdd6()
//		    .insertPOSNacionalExtractAdd7()
//		    .insertPOSNacionalExtractAdd8()
//		    .insertPOSNacionalExtractAdd9()
//		    .insertPOSNacionalExtractAdd10()
//		    .insertPOSNacionalExtractAdd11()
//		    .insertPOSNacionalExtractAdd12()
//		    .insertPOSNacionalExtractAdd13()
//		    .insertPOSNacionalExtractAdd14()
//		    .insertPOSNacionalExtractAdd15()
//		    .insertPOSNacionalExtractAdd16()
//		    .insertPOSNacionalExtractAdd17()
//		    .insertPOSNacionalExtractAdd18();
			//new ParserRulesCreator().drop().create().insertAll();
			//new SimulatorCreator().drop().create().insertAll();
			//new SimulatorRulesCreator().drop().create().insertAll();
			//new FieldFormatCreator().insertPOSTpvs();
			//new FieldFormatCreator().insertVisa();
			//new DataBaseCreator().drop().create().insertAll();
			//new FtpCreator().drop().create().insertAll();
			//new ExtractorWindowCreator().drop().create().insertAll();
			//insertFTPConnections();
			//new PreferencesCreator().create().insertAll();
			//new EnviromentTypeCreator().drop().create().insertAll();
			//new DataBaseCreator().drop().create().insertAll();
			//new FtpCreator().drop().create().insertAll();
			//new EnviromentCreator().drop().create().insertAll();
			//new FieldFormatGroupCreator().drop().create().insertAll();
			//new FieldFormatCreator().insertPOSNacionalStratus();
			//new SimulatorCreator().drop().create().insertAll();
			//new AdapterRulesCreator().drop().create().insertAll();
			//new SimulatorRulesCreator().drop().create().insertAll();
			//new ParserRulesCreator().drop().create().insertAll();
			//new MessageIsoCreator().create().insertAll();
			//new MessageIsoCreator().update();
			//new MessageIsoCreator().backup(new java.io.File("C:\\eclipse\\messages.txt"));
			//new TransactionTypeCreator().drop().create().insertAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public Creator close() throws IOException {
		if (connection != null)
			connection.close();
		return this;
	}
	public static void main(String[] args) throws Exception {
		new Creator()
		.createDataBase()
		.close();
	}
}
