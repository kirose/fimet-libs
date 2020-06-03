package com.fimet.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.dao.IParserDAO;
import com.fimet.AbstractManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.mx.AmexParser;
import com.fimet.parser.mx.DiscoverParser;
import com.fimet.parser.mx.LayoutParser;
import com.fimet.parser.mx.JcbParser;
import com.fimet.parser.mx.MasterCardParser;
import com.fimet.parser.mx.NationalParser;
import com.fimet.parser.mx.TpvParser;
import com.fimet.parser.mx.VisaParser;

public class ParserManager extends AbstractManager implements IParserManager {
	Map<String,IParser> mapNameParsers = new HashMap<>();
	IParserDAO dao = Manager.getExtension(IParserDAO.class, ParserDAO.class);
	public ParserManager() {
		super();
	}
	@Override
	public void reload() {
		mapNameParsers.clear();
	}
	@Override
	public IParser getParser(String name) {
		if (mapNameParsers.containsKey(name)) {
			return mapNameParsers.get(name);
		} else {
			IEParser entity = findEntity(name);
			IParser parser = newParser(entity);
			mapNameParsers.put(name, parser);
			return parser;
		}
	}
	@Override
	public IParser getParser(IEParser entity) {
		if (entity.getName() != null) {
			if (mapNameParsers.containsKey(entity.getName())) {
				return mapNameParsers.get(entity.getName());
			} else {
				return getParser(entity.getName());
			}
		} else {
			throw new ParserException("Iparser and name is null, invalid entity "+entity);
		}
	}
	private IEParser findEntity(String parser) {
		IEParser entity = dao.getByName(parser);
		if (entity != null) {
			return entity;
		} else {
			throw new ParserException("Unknow parser: "+parser);
		}
	}
	@SuppressWarnings("unchecked")
	private IParser newParser(IEParser entity) {
		try {
			Class<? extends AbstractMessageBitmapParser> parserClass = (Class<? extends AbstractMessageBitmapParser>) Class.forName(entity.getParserClass());
			Constructor<? extends AbstractMessageBitmapParser> constructor = parserClass.getConstructor(com.fimet.parser.IEParser.class);
			return constructor.newInstance(entity);
		} catch (ClassNotFoundException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (NoSuchMethodException e) {
			throw new ParserException("No found public constructor with "+IEParser.class.getName()+" as argument in class: " + entity.getParserClass(),e);
		} catch (SecurityException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		}
	}

	@Override
	public List<IParser> getParsers() {
		List<IEParser> entities = dao.getAll();
		if (entities != null) {
			List<IParser> ps = new ArrayList<>();
			for (IEParser e : entities) {
				if (mapNameParsers.containsKey(e.getName())) {
					ps.add(mapNameParsers.get(e.getName()));
				} else {
					ps.add(getParser(e));
				}
			}
			return ps;
		} else {
			return null;
		}
	}
	private IParser uninstall(String parser) {
		if (mapNameParsers.containsKey(parser)) {
			IParser p = mapNameParsers.remove(parser);
			return p;
		} else {
			return null;
		}
	}
	private IParser install(String parser) {
		return getParser(parser);
	}
	@Override
	public void reload(String parser) {
		uninstall(parser);
		install(parser);
	}
	@Override
	public Class<?>[] getParserClasses() {
		return new Class<?>[] {
			NationalParser.class,
			VisaParser.class,
			MasterCardParser.class,
			TpvParser.class,
			DiscoverParser.class,
			AmexParser.class,
			JcbParser.class,
			LayoutParser.class
		};
	}
}
