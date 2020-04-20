package com.fimet.core.iso8583.parser;

import java.util.List;

import com.google.gson.annotations.JsonAdapter;
import com.fimet.core.iso8583.parser.MessageFields.Node;
import com.fimet.core.json.adapter.JsonAdapterFactory;
import com.fimet.core.json.adapter.MessageLayoutAdapter;
import com.fimet.core.iso8583.adapter.IAdapter;
import com.fimet.core.iso8583.parser.IParser;

@JsonAdapter(MessageLayoutAdapter.class)
public class MessageLayout implements IMessage {
	private IParser parser;
	protected IAdapter adapter;
	private MessageFields fields;
	public MessageLayout() {
		fields = new MessageFields(this);
	}
	public IAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(IAdapter adapter) {
		this.adapter = adapter;
	}
	public IParser getParser() {
		return parser;
	}
	public void setParser(IParser parser) {
		this.parser = parser;
		if (parser != null) {
			fields.idGroup = parser.getIdGroup();
		}
	}
	public MessageFields getFields() {
		return fields;
	}
	public void setFields(MessageFields fields) {
		this.fields = fields;
	}
	@Override
	public boolean hasChildren(String idField) {
		return fields.hasChildren(idField);
	}
	@Override
	public byte[] getField(String idField) {
		return fields.get(idField);
	}
	@Override
	public byte[] getField(int idField) {
		return fields.get(idField+"");
	}
	@Override
	public boolean hasValue(String idField) {
		return fields.find(idField) != null;
	}
	@Override
	public boolean hasValue(int idField) {
		return fields.find(idField+"") != null;
	}
	@Override
	public boolean hasField(String idField) {
		return fields.find(idField) != null;
	}
	@Override
	public boolean hasField(int idField) {
		return fields.find(idField+"") != null;
	}
	@Override
	public List<String> getIdChildren(String idField) {
		return fields.getIdChildren(idField);
	}
	@Override
	public void setField(String idField, byte[] value) {
		fields.insert(idField, value);
	}
	@Override
	public String getValue(String idField) {
		Node node = fields.find(idField);
		return node != null ? node.getValue() : null;
	}
	@Override
	public String toJson() {
		return JsonAdapterFactory.GSON.toJson(this);
	}
	public void setValue(String idField, String value) {
		fields.insert(idField, value == null ? null : value.getBytes());
	}
	public List<Field> getRootFields() {
		return new FieldTree(fields).getRoots();
	}
	@Override
	public String getValue(int idField) {
		return getValue(""+idField);
	}
	
}
