package com.fimet.commons.data.reader;

import com.fimet.commons.converter.IConverter;

/**
 * 
 * @author Marco Antonio
 *
 */
public interface IReader {
	IMatcher matcher(String match);
	int length();
	int current();
	void move(int no);
	void assertChar(char c);
	boolean hasNext();
	byte[] getBytes(int length);
	byte[] read(int length);
	void convert(IConverter converter);
}
