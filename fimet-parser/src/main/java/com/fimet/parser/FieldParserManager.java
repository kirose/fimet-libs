package com.fimet.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.exception.ParserException;
import com.fimet.IFieldFormatManager;
import com.fimet.IFieldParserManager;
import com.fimet.Manager;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.entity.sqlite.EFieldFormatGroup;
import com.fimet.iso8583.parser.IFieldParser;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.persistence.dao.FieldFormatDAO;
import com.fimet.parser.field.FixedFieldParser;
import com.fimet.parser.field.LTrimFixedFieldParser;
import com.fimet.parser.field.LTrimVarFieldParser;
import com.fimet.parser.field.RTrimFixedFieldParser;
import com.fimet.parser.field.RTrimVarFieldParser;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.parser.field.mc.MC48VarFieldParser;
import com.fimet.parser.field.mc.MCTagVarFieldParser;
import com.fimet.parser.field.mc.MCTagsVarFieldParser;
import com.fimet.parser.field.mx.NatTagFixedFieldParser;
import com.fimet.parser.field.mx.NatTagVarFieldParser;
import com.fimet.parser.field.mx.NatTagsVarFieldParser;
import com.fimet.parser.field.mx.NatTokenEZVarFieldParser;
import com.fimet.parser.field.mx.NatTokenR1VarFieldParser;
import com.fimet.parser.field.mx.NatTokenVarFieldParser;
import com.fimet.parser.field.mx.NatTokensVarFieldParser;
import com.fimet.parser.field.tpv.TpvTag55VarFieldParser;
import com.fimet.parser.field.tpv.TpvTag56VarFieldParser;
import com.fimet.parser.field.tpv.TpvTags55VarFieldParser;
import com.fimet.parser.field.tpv.TpvTags56VarFieldParser;
import com.fimet.parser.field.tpv.TpvTokenEZVarFieldParser;
import com.fimet.parser.field.tpv.TpvTokenQKVarFieldParser;
import com.fimet.parser.field.tpv.TpvTokenVarFieldParser;
import com.fimet.parser.field.tpv.TpvTokensVarFieldParser;
import com.fimet.parser.field.visa.Visa126VarFieldParser;
import com.fimet.parser.field.visa.Visa62VarFieldParser;
import com.fimet.parser.field.visa.Visa63VarFieldParser;
import com.fimet.parser.field.visa.VisaDatasetVarFieldParser;
import com.fimet.parser.field.visa.VisaDatasetsVarFieldParser;
import com.fimet.parser.field.visa.VisaTagVarFieldParser;

public class FieldParserManager implements IFieldParserManager {
	private IFieldFormatManager fieldFormatGroupManager = Manager.get(IFieldFormatManager.class); 
	private Map<String, IFieldParser> cache = new HashMap<>();
	
	public FieldParserManager() {
		super();
	}
	/**
	 * 
	 * @param idParser
	 * @param idField
	 * @param reader
	 * @return
	 */
	public byte[] parse(Integer idField, IReader reader, IMessage message) {
		return getFieldParser(message.getParser().getIdGroup(), idField+"").parse(reader, message);
	}
	/**
	 * 
	 * @param msgParser
	 * @param index
	 * @return
	 */
	public byte[] parse(String idField, IReader reader, IMessage message) {
		return getFieldParser(message.getParser().getIdGroup(), idField).parse(reader, message);
	}
	/**
	 * 
	 * @param msgParser
	 * @param index
	 * @return
	 */
	public void format(IMessage message, String idField, IWriter writer) {
		getFieldParser(message.getParser().getIdGroup(), idField).format(writer, message);
	}
	public void format(IMessage message, int idField, IWriter writer) {
		getFieldParser(message.getParser().getIdGroup(), ""+idField).format(writer, message);
	}
	public IFieldParser getFieldParser(Integer idGroup, String idField) {
		return getFieldParser(fieldFormatGroupManager.getGroup(idGroup), idField);
	}
	/**
	 * 
	 * @param idField
	 * @return
	 */
	public IFieldParser getFieldParser(EFieldFormatGroup group, String idField) {
		if (cache.containsKey(group.getId() + "." + idField)) {
			return cache.get(group.getId() + "." + idField);
		} else {
			EFieldFormat fieldFormat = FieldFormatDAO.getInstance().findByGroupAndIdField(group.getId(),idField);
			if (fieldFormat != null) {
				try {
					IFieldParser fieldParser = null;
					Class<?> classParser = Class.forName(fieldFormat.getClassParser());
					fieldParser = (IFieldParser) classParser.getConstructor(EFieldFormat.class).newInstance(fieldFormat);
					cache.put(group.getId() + "." + idField, fieldParser);
					return fieldParser;
				} catch (Exception e) {
					throw new ParserException(group+" cannot found Parser: "+fieldFormat.getClassParser(),e);
				}
			} else {
				if (group.getParent() == null) {
					return null;
					//throw new ParserException(group+" cannot found Field Format: "+idField);
				} else {
					IFieldParser fieldParser = getFieldParser(group.getParent(), idField);
					if (fieldParser != null) {
						cache.put(group.getId() + "." + idField, fieldParser);
					}
					return fieldParser;
				}
			}
		}
	}
	
	public List<IFieldParser> getRootFieldParsers(Integer idGroup){
		return getRootFieldParsers(fieldFormatGroupManager.getGroup(idGroup));
	}
	/**
	 * For extract
	 * @param group
	 * @return
	 */
	public List<IFieldParser> getRootFieldParsers(EFieldFormatGroup group){
		loadGroup(group);
		String id = group.getId()+".";
		List<IFieldParser> parsers = new ArrayList<IFieldParser>();
		for (Map.Entry<String, IFieldParser> e : cache.entrySet()) {
			if (e.getKey().startsWith(id) && e.getKey().indexOf('.', id.length()+1) == -1) {
				parsers.add(e.getValue());
				
			}
		}
		Collections.sort(parsers, (IFieldParser f1, IFieldParser f2)->{
			return f1.getIdOrder().compareTo(f2.getIdOrder());
		});
		return parsers;
	}
	private boolean isLoadedGroup(EFieldFormatGroup group) {
		String id = group.getId()+".";
		for (Map.Entry<String, IFieldParser> e : cache.entrySet()) {
			if (e.getKey().startsWith(id)) {
				return true;
			}
		}
		return false;
	}
	private void loadGroup(EFieldFormatGroup group) {
		if (!isLoadedGroup(group)) {
			List<EFieldFormat> formats = FieldFormatDAO.getInstance().findByGroup(group.getId());
			for (EFieldFormat format : formats) {
				if (!cache.containsKey(group.getId() + "." + format.getIdField())) {
					try {
						IFieldParser fieldParser = null;
						Class<?> classParser = Class.forName(format.getClassParser());
						fieldParser = (IFieldParser) classParser.getConstructor(EFieldFormat.class).newInstance(format);
						cache.put(group.getId() + "." + format.getIdField(), fieldParser);
					} catch (Exception e) {
						throw new ParserException(group+" cannot found Parser: "+format.getClassParser(),e);
					}
				}		
			}
		}
		if (group.getParent() != null) {
			loadGroup(group);
		}
	}
	/**
	 * 
	 * @param idGroup
	 */
	public void deleteCache(Integer idGroup) {
		if (!cache.isEmpty() && idGroup != null) {
			EFieldFormatGroup group = fieldFormatGroupManager.getGroup(idGroup);
			deleteCache(group);
		}
	}
	public void deleteCache(EFieldFormatGroup group) {
		if (!cache.isEmpty() && group != null) {
			_deleteCache(group);
			List<EFieldFormatGroup> subGroups = fieldFormatGroupManager.getSubGroups(group.getId());
			if (subGroups != null && !subGroups.isEmpty()) {
				for (EFieldFormatGroup subgroup : subGroups) {
					deleteCache(subgroup);
				}
			}
		}
	}
	private void _deleteCache(EFieldFormatGroup group) {
		Map<String, IFieldParser> newCache = new HashMap<>();
		List<String> starts = new ArrayList<String>();
		while (group != null) {
			starts.add(group.getId()+".");
			group = group.getParent();
		}
		boolean startsWith = false;
		for (Map.Entry<String, IFieldParser> e : cache.entrySet()) {
			startsWith = false;
			for (String s : starts) {
				if (e.getKey().startsWith(s)) {
					startsWith = true;
					break;
				}
			}
			if (!startsWith) {
				newCache.put(e.getKey(), e.getValue());
			}
		}
		cache = newCache;
	}
	@Override
	public void free() {}
	@Override
	public void saveState() {}
	public Class<?>[] getParserClasses() {
		return new Class<?>[] {
			FixedFieldParser.class,
			VarFieldParser.class,
			LTrimVarFieldParser.class,
			LTrimFixedFieldParser.class,
			RTrimVarFieldParser.class,
			RTrimFixedFieldParser.class,
			NatTagFixedFieldParser.class,
			NatTagsVarFieldParser.class,
			NatTagVarFieldParser.class,
			NatTokenR1VarFieldParser.class,
			NatTokenEZVarFieldParser.class,
			NatTokensVarFieldParser.class,
			NatTokenVarFieldParser.class,
			TpvTag55VarFieldParser.class,
			TpvTag56VarFieldParser.class,
			TpvTags55VarFieldParser.class,
			TpvTags56VarFieldParser.class,
			TpvTokenEZVarFieldParser.class,
			TpvTokenQKVarFieldParser.class,
			TpvTokensVarFieldParser.class,
			TpvTokenVarFieldParser.class,
			Visa126VarFieldParser.class,
			Visa62VarFieldParser.class,
			Visa63VarFieldParser.class,
			VisaDatasetsVarFieldParser.class,
			VisaDatasetVarFieldParser.class,
			VisaTagVarFieldParser.class,
			MC48VarFieldParser.class,
			MCTagsVarFieldParser.class,
			MCTagVarFieldParser.class
		};
	}
}
