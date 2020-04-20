package com.fimet.commons.data.reader.impl;


import com.fimet.commons.converter.IConverter;
import com.fimet.commons.data.reader.IMatcher;
import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.exception.FimetException;
import com.fimet.commons.utils.ByteUtils;
/**
 * The message in bytes 
 * @author Marco Antonio
 *
 */
public class ByteArrayReader implements IReader {

	private byte[] bytes;
	private int cursor;
	private int length;
	
	public ByteArrayReader(byte[] bytes){
		if (bytes == null) {
			throw new NullPointerException("bytes are null");
		}
		this.bytes = bytes;
		this.cursor = 0;
		this.length = bytes.length;
	}
	public byte[] getBytes(int length) {
		if (cursor < this.length) {
			byte[] bytes = new byte[minLength(length)];
			System.arraycopy(this.bytes, cursor, bytes, 0, minLength(length));
			return bytes;
		} else {
			return new byte[0];
		}
	}
	@Override
	public byte[] read(int length) {
		if (cursor < this.length) {
			byte[] bytes = new byte[length = minLength(length)];
			System.arraycopy(this.bytes, cursor, bytes, 0, length);
			move(length);
			return bytes;
		} else {
			return new byte[0];
		}
	}
	@Override
	public void convert(IConverter converter) {
		if (cursor < this.length) {
			byte[] converted = converter.convert(ByteUtils.subArray(this.bytes, cursor));
			bytes = ByteUtils.concat(ByteUtils.subArray(bytes, 0, cursor), converted);
			length = bytes.length;
		}
	}
	private int minLength(int length) {
		return Math.min(length, this.length-cursor);
	}
	public IMatcher matcher(String text) {
		return new MsgMatcher(this, text.getBytes());
	}
	public void assertChar(char c) {
		if (c != ((char) bytes[cursor])) {
			throw new FimetException("Expected char '"+c+"' found: '"+((char) bytes[cursor])+"'");
		}
		cursor++;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = cursor; i < bytes.length; i++) {
			sb.append((char)bytes[i]);
		}
		return sb.toString();
	}
	public boolean hasNext() {
		return cursor < length;
	}
	public int length() {
		return length;
	}
	public int current() {
		return cursor;
	}
	public void move(int no) {
		if (no == 0) {
			return;
		}
		if (cursor + no > length || cursor + no < 0) {
			throw new IndexOutOfBoundsException();
		}
		cursor += no;
	}
	public static class MsgMatcher implements IMatcher {
		private ByteArrayReader msgBuffered;
		private byte[] bytes;
		private int start;
		private int end;
		private MsgMatcher(ByteArrayReader buf, byte[] bytes) {
			if (bytes == null) {
				throw new NullPointerException();
			}
			this.msgBuffered = buf;
			this.start = buf.cursor;
			this.end = start + bytes.length;
			this.bytes = bytes;

			if (start < 0) {
				throw new IndexOutOfBoundsException();
			}
			if (end <= 0) {
				throw new IndexOutOfBoundsException();
			}
			if (end > buf.length) {
				throw new IndexOutOfBoundsException();
			}
		}
		public boolean asByte() {
			for (int i = start; i < end; i++) {
				if (msgBuffered.bytes[i] != bytes[i-start]) {
					return false;
				}
			}
			return true;
		}
	}
}
