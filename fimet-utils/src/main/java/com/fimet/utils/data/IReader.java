package com.fimet.utils.data;

import com.fimet.utils.converter.IConverter;

/**
 * 
 * @author Marco Antonio
 *
 */
public interface IReader {
	boolean startsWith(String text);
	int length();
	int position();
	void move(int offset);
	void assertChar(char c);
	boolean hasNext();
	byte[] peek(int length);
	byte[] read(int length);
	void convert(IConverter converter);
}
