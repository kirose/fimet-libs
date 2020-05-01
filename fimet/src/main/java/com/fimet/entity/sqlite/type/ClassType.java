package com.fimet.entity.sqlite.type;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class ClassType extends StringType {

	 private static ClassType instance;
	 public static ClassType getSingleton() {
		if (instance == null) {
			instance = new ClassType();
		}
		return instance;
	 }
	 protected ClassType() {
		super(SqlType.STRING, new Class<?>[] {Class.class});
	 }
	 @Override
	 public Object javaToSqlArg(FieldType type, Object obj) {
		return obj != null ? ((Class<?>) obj).getName() : null;
	 }
	 @Override
	 public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		try {
			return sqlArg != null ? Class.forName((String)sqlArg) : null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	 }
}
