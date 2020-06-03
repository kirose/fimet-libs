package com.fimet.sqlite.type;

import java.lang.reflect.Type;
import java.util.List;

import com.fimet.sqlite.usecase.Simulator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class ArraySimulatorType extends StringType {

	private static ArraySimulatorType instance;
	private Gson gson;
	public static ArraySimulatorType getSingleton() {
		if (instance == null) {
			instance = new ArraySimulatorType();
		}
		return instance;
	}
	protected ArraySimulatorType() {
		super(SqlType.STRING, new Class<?>[] {Simulator.class});
		gson = new Gson();
	}
	 @Override
	 public Object javaToSqlArg(FieldType type, Object obj) {
		 return obj != null ? taskIssuerToJson((Simulator[]) obj) : null;
	 }
	 @Override
	 public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		 return sqlArg != null ? jsonToField((String) sqlArg) : null;
	 }
	 private String taskIssuerToJson(Simulator[] obj) {
		 return getSingleton().gson.toJson(obj);
	 }
	 private List<Simulator> jsonToField(String json) {
		 Type type = new TypeToken<Simulator[]>(){}.getType();
		 return getSingleton().gson.fromJson(json, type);
	 }
}
