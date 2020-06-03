package com.fimet.sqlite.type;


import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.simulator.ISimulator;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class SimulatorType extends StringType {

	 private static SimulatorType instance;
	 public static SimulatorType getSingleton() {
		if (instance == null) {
			instance = new SimulatorType();
		}
		return instance;
	 }
	 protected SimulatorType() {
		super(SqlType.STRING, new Class<?>[] {ISimulator.class});
	 }
	 @Override
	 public Object javaToSqlArg(FieldType type, Object obj) {
		return obj != null ? ((ISimulator) obj).getName() : null;
	 }
	 @Override
	 public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		return sqlArg != null ? Manager.get(ISimulatorManager.class).getSimulator((String)sqlArg) : null;
	 }
}
