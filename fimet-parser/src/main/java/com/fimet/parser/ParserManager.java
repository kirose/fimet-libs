package com.fimet.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.dao.ParserDAO;
import com.fimet.entity.EParser;
import com.fimet.AbstractManager;
import com.fimet.IParserManager;
import com.fimet.parser.mx.AmexParser;
import com.fimet.parser.mx.DiscoverParser;
import com.fimet.parser.mx.LayoutParser;
import com.fimet.parser.mx.JcbParser;
import com.fimet.parser.mx.MasterCardParser;
import com.fimet.parser.mx.NacionalParser;
import com.fimet.parser.mx.TpvParser;
import com.fimet.parser.mx.VisaParser;
import com.fimet.utils.History;

public class ParserManager extends AbstractManager implements IParserManager {
	Map<Integer,IParser> mapIdParsers = new HashMap<>();
	Map<String,Integer> mapNameId = new HashMap<>();
	Map<Integer,IParser> mapHashParsers = new HashMap<>();
	public ParserManager() {
		super();
	}

	@Override
	public IParser getParser(String name) {
		if (mapNameId.containsKey(name)) {
			return mapIdParsers.get(mapNameId.get(name));
		} else {
			EParser entity = findEntity(name);
			IParser parser = newParser(entity);
			mapIdParsers.put(parser.getId(), parser);
			mapNameId.put(parser.getName(),parser.getId());
			mapHashParsers.put(entity.hashCode(), parser);
			return parser;
		}
	}

	@Override
	public IParser getParser(Integer idParser) {
		if (mapIdParsers.containsKey(idParser)) {
			return mapIdParsers.get(idParser);
		} else {
			EParser entity = findEntity(idParser);
			IParser parser = newParser(entity);
			mapIdParsers.put(parser.getId(), parser);
			mapNameId.put(parser.getName(),parser.getId());
			mapHashParsers.put(entity.hashCode(), parser);
			return parser;
		}
	}
	@Override
	public IParser getParser(EParser entity) {
		int hashCode = entity.hashCode();
		if (mapHashParsers.containsKey(hashCode)) {
			return mapHashParsers.get(hashCode);
		} else {
			IParser parser = newParser(entity);
			mapIdParsers.put(parser.getId(), parser);
			mapHashParsers.put(hashCode,parser);
			return parser;			
		}
	}
	private EParser findEntity(Integer idParser) {
		EParser entity = ParserDAO.getInstance().findById(idParser);
		if (entity != null) {
			return entity;
		} else {
			throw new ParserException("Invalid parser id: "+idParser);
		}
	}
	private EParser findEntity(String parser) {
		EParser entity = ParserDAO.getInstance().findByName(parser);
		if (entity != null) {
			return entity;
		} else {
			throw new ParserException("Unknow parser: "+parser);
		}
	}
	@SuppressWarnings("unchecked")
	private IParser newParser(EParser entity) {
		try {
			Class<? extends AbstractMessageBitmapParser> parserClass = (Class<? extends AbstractMessageBitmapParser>) Class.forName(entity.getParserClass());
			Constructor<? extends AbstractMessageBitmapParser> constructor = parserClass.getConstructor(com.fimet.entity.EParser.class);
			return constructor.newInstance(entity);
		} catch (ClassNotFoundException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (NoSuchMethodException e) {
			throw new ParserException("No found public constructor with "+EParser.class.getName()+" as argument in class: " + entity.getParserClass(),e);
		} catch (SecurityException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		}
	}

	@Override
	public List<IParser> getParsers() {
		List<EParser> entities = ParserDAO.getInstance().findAll();
		if (entities != null) {
			List<IParser> ps = new ArrayList<>();
			for (EParser e : entities) {
				if (mapIdParsers.containsKey(e.getId())) {
					ps.add(mapIdParsers.get(e.getId()));
				} else {
					ps.add(getParser(e));
				}
			}
			return ps;
		} else {
			return null;
		}
	}
	private IParser uninstall(Integer idParser) {
		if (mapIdParsers.containsKey(idParser)) {
			IParser pold = mapIdParsers.remove(idParser);
			mapNameId.remove(pold.getName());
			mapHashParsers.remove(pold.hashCode());
			return pold;
		} else {
			return null;
		}
	}
	private IParser install(Integer idParser) {
		if (!mapIdParsers.containsKey(idParser)) {
			IParser parser = getParser(idParser);
			return parser;
		} else {
			return getParser(idParser);
		}
	}
	@Override
	public void reload(Integer idParser) {
		uninstall(idParser);
		install(idParser);
	}
	@Override
	public List<EParser> getEntities() {
		return ParserDAO.getInstance().findAll();
	}
	@Override
	public List<EParser> getEntities(int type) {
		return ParserDAO.getInstance().findByType(type);
	}
	@Override
	public EParser insert(EParser parser) {
		if (parser.getId() == null)
			parser.setId(getNextIdParser());
		ParserDAO.getInstance().insert(parser);
		return parser;
	}
	@Override
	public EParser update(EParser parser) {
		ParserDAO.getInstance().update(parser);
		reload(parser.getId());
		return parser;
	}
	@Override
	public EParser delete(EParser parser) {
		ParserDAO.getInstance().delete(parser);
		uninstall(parser.getId());
		return parser;
	}
	@Override
	public Integer getNextIdParser() {
		return ParserDAO.getInstance().getNextSequenceId();
	}
	@Override
	public Integer getPrevIdParser() {
		return ParserDAO.getInstance().getPrevSequenceId();
	}

	@Override
	public EParser getEntity(Integer id) {
		return ParserDAO.getInstance().findById(id);
	}

	@Override
	public void commit(History<EParser> history) {
		for (EParser m : history.getDeletes()) {
			delete(m);
		}
		for (EParser m : history.getUpdates()) {
			update(m);
		}
		for (EParser m : history.getInserts()) {
			insert(m);
		}		
	}
	@Override
	public Class<?>[] getParserClasses() {
		return new Class<?>[] {
			NacionalParser.class,
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
