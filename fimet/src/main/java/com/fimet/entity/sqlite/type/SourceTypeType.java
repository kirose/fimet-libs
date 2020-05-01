package com.fimet.entity.sqlite.type;


import com.fimet.ISourceManager;
import com.fimet.Manager;
import com.fimet.entity.sqlite.ESourceType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.IntType;

public class SourceTypeType extends IntType {

	 private static SourceTypeType instance;
	 public static SourceTypeType getSingleton() {
		if (instance == null) {
			instance = new SourceTypeType();
		}
		return instance;
	 }
	 protected SourceTypeType() {
		super(SqlType.INTEGER, new Class<?>[] {ESourceType.class});
	 }
	 @Override
	 public Object javaToSqlArg(FieldType type, Object obj) {
		 if (obj instanceof ESourceType) {
			 return ((ESourceType) obj).getId();
		 } else if (obj instanceof Integer) {
			 return (Integer) obj;
		 } else {
			 return null;
		 }
	 }
	 @Override
	 public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		return sqlArg != null ? Manager.get(ISourceManager.class).getSourceType((Integer)sqlArg) : null;
	 }
}
