package com.fimet.sqlite.type;


import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.IParser;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class ParserType extends StringType {

	 private static ParserType instance;
	 public static ParserType getSingleton() {
		if (instance == null) {
			instance = new ParserType();
		}
		return instance;
	 }
	 protected ParserType() {
		super(SqlType.STRING, new Class<?>[] {IParser.class});
	 }
	 @Override
	 public Object javaToSqlArg(FieldType type, Object obj) {
		return obj != null ? ((IParser) obj).getName() : null;
	 }
	 @Override
	 public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
		return sqlArg != null ? Manager.get(IParserManager.class).getParser((String)sqlArg) : null;
	 }
}
