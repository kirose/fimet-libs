package com.fimet.parser;

import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public class FieldParserWapper implements IFieldParser {
	private IFieldParser wrapped;
	public FieldParserWapper(IFieldParser wrapped) {
		this.wrapped = wrapped;
	}
	public IFieldParser getWrapped() {
		return wrapped;
	}
	public void setWrapped(IFieldParser wrapped) {
		this.wrapped = wrapped;
	}
	@Override
	public String getName() {
		return wrapped.getName();
	}

	@Override
	public String getIdField() {
		return wrapped.getIdField();
	}

	@Override
	public String getIdOrder() {
		return wrapped.getIdOrder();
	}

	@Override
	public String getType() {
		return wrapped.getType();
	}

	@Override
	public int getLength() {
		return wrapped.getLength();
	}

	@Override
	public boolean isValidValue(String value) {
		return wrapped.isValidValue(value);
	}

	@Override
	public boolean isValidLength(String value) {
		return wrapped.isValidLength(value);
	}

	@Override
	public short[] getAddress() {
		return wrapped.getAddress();
	}

	@Override
	public byte[] parse(IReader reader, IMessage message) {
		return wrapped.parse(reader, message);
	}

	@Override
	public byte[] format(IWriter writer, IMessage message) {
		return wrapped.format(writer, message);
	}

	@Override
	public boolean hasChild(String idChild) {
		return wrapped.hasChild(idChild);
	}

	@Override
	public int indexOfChild(String idChild) {
		return wrapped.indexOfChild(idChild);
	}

	@Override
	public String getIdChild(int index) {
		return wrapped.getIdChild(index);
	}
	@Override
	public String toString() {
		return wrapped.toString();
	}	
}
