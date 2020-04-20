package com.fimet.cfg.db;

import static com.fimet.core.entity.sqlite.Simulator.*;

import java.sql.SQLException;

import com.fimet.core.entity.sqlite.Simulator;
import com.fimet.core.entity.sqlite.SimulatorMessage;
import com.fimet.core.persistence.dao.SimulatorDAO;
import com.fimet.core.persistence.dao.SimulatorMessageDAO;
import com.fimet.simulator.SimulatorImpl;
import com.fimet.simulator.field.*;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class SimulatorCreator implements ICreator {
	
	
	
	SimulatorMessageDAO daoMsg;
	SimulatorDAO dao;
	ConnectionSource connection;
	public SimulatorCreator(ConnectionSource connection) {
		super();
		this.connection = connection;
		dao = new SimulatorDAO(connection);
		daoMsg = new SimulatorMessageDAO(connection);
	}
	public SimulatorCreator create() throws SQLException {
		TableUtils.createTableIfNotExists(connection, Simulator.class);
		TableUtils.createTableIfNotExists(connection, SimulatorMessage.class);
		return this;
	}
	public SimulatorCreator drop() throws SQLException {
		TableUtils.dropTable(connection, Simulator.class, true);
		TableUtils.dropTable(connection, SimulatorMessage.class, true);
		return this;
	}
	public SimulatorCreator insertAll() {
		SimulatorDAO dao = new SimulatorDAO(connection);
		SimulatorMessageDAO daoMsg = new SimulatorMessageDAO(connection);
		int id = 0;
		//SimulatorImpl simulatorIssuer = new SimulatorImpl(id, "National Issuer");
		dao.insertOrUpdate(new Simulator(id,"National Issuer",ISSUER));
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0200", SimulatorMessage.RESPONSE,"")
			.addIncludeFieldCls("15", SetNewDateMMdd.class)
			.addIncludeFieldCls("38", SetRandom6N.class)
			.addIncludeFieldFix("39", "00")
		);
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0220", SimulatorMessage.RESPONSE,"12,13,15,17,38,43,48,60,62")
			.addIncludeFieldFix("39", "00")
		);
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0221", SimulatorMessage.RESPONSE,"12,13,15,17,38,43,48,60,62")
			.addIncludeFieldFix("39", "00")
		);
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0420", SimulatorMessage.RESPONSE, "12,13,15,17,38,43,48,60,62")
			.addIncludeFieldFix("39", "00")
		);
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0421", SimulatorMessage.RESPONSE, "12,13,15,17,38,43,48,60,62")
			.addIncludeFieldFix("39", "00")
		);
		daoMsg.insertOrUpdate(
			new SimulatorMessage(id, "0800", SimulatorMessage.RESPONSE, "")
			.addIncludeFieldFix("39", "00")
		);
		
		id = 1;
		//SimulatorImpl simulatorAcqurier = new SimulatorImpl(id, "National Acquirer");
		dao.insert(new Simulator(id,"National Acquirer",ACQUIRER));
		daoMsg.insert(
				new SimulatorMessage(id, "0100", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("2", IfHasSetCorrectPanLastDigit.class)
				.addIncludeFieldCls("7", IfHasSetNewDateMMddhhmmss.class)
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
				.addIncludeFieldCls("13", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("15", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("16", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("17", IfHasSetNewDateMMdd.class)
			);
			daoMsg.insertOrUpdate(
				new SimulatorMessage(id, "0200", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("2", IfHasSetCorrectPanLastDigit.class)
				.addIncludeFieldCls("7", IfHasSetNewDateMMddhhmmss.class)
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
				.addIncludeFieldCls("13", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("15", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("16", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("17", IfHasSetNewDateMMdd.class)
			);
			daoMsg.insertOrUpdate(
				new SimulatorMessage(id, "0220", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("2", IfHasSetCorrectPanLastDigit.class)
				.addIncludeFieldCls("7", IfHasSetNewDateMMddhhmmss.class)
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
				.addIncludeFieldCls("13", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("15", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("16", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("17", IfHasSetNewDateMMdd.class)
			);
			daoMsg.insertOrUpdate(
				new SimulatorMessage(id, "0420", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("2", IfHasSetCorrectPanLastDigit.class)
				.addIncludeFieldCls("7", IfHasSetNewDateMMddhhmmss.class)
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
				.addIncludeFieldCls("13", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("15", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("16", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("17", IfHasSetNewDateMMdd.class)
			);
			daoMsg.insertOrUpdate(
				new SimulatorMessage(id, "0421", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("2", IfHasSetCorrectPanLastDigit.class)
				.addIncludeFieldCls("7", IfHasSetNewDateMMddhhmmss.class)
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
				.addIncludeFieldCls("13", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("15", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("16", IfHasSetNewDateMMdd.class)
				.addIncludeFieldCls("17", IfHasSetNewDateMMdd.class)
			);
			daoMsg.insertOrUpdate(
				new SimulatorMessage(id, "0800", SimulatorMessage.REQUEST, "")
				.addIncludeFieldCls("12", IfHasSetNewDatehhmmss.class)
			);		

		return this;
	}
}

