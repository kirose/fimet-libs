package com.fimet.utils;


import com.fimet.utils.converter.IConverter;
/**
 * The message in bytes 
 * @author Marco Antonio
 *
 */
public class ByteBuffer implements IReader {

	private byte[] bytes;
	private int cursor;
	private int length;
	
	public ByteBuffer(byte[] bytes){
		if (bytes == null) {
			throw new NullPointerException("bytes are null");
		}
		this.bytes = bytes;
		this.cursor = 0;
		this.length = bytes.length;
	}
	@Override
	public byte[] peek(int length) {
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
	@Override
	public void assertChar(char c) {
		if (c != ((char) bytes[cursor])) {
			throw new AssertionError("Expected char '"+c+"' found: '"+((char) bytes[cursor])+"'");
		}
		cursor++;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = cursor; i < bytes.length; i++) {
			sb.append((char)bytes[i]);
		}
		return sb.toString();
	}
	@Override
	public boolean hasNext() {
		return cursor < length;
	}
	@Override
	public int length() {
		return length;
	}
	@Override
	public int position() {
		return cursor;
	}
	@Override
	public void move(int no) {
		if (no == 0) {
			return;
		}
		if (cursor + no > length || cursor + no < 0) {
			throw new IndexOutOfBoundsException();
		}
		cursor += no;
	}
	public boolean startsWith(String value) {
		return startsWith(value.getBytes());
	}
	public boolean startsWith(byte[] value) {
		return startsWith(value, cursor);
	}
	public boolean startsWith(byte[] value, int offset) {
		int index = 0;
		int length = value.length;
		if (offset >= 0 && offset <= bytes.length - length) {
			do {
				--length;
				if (length < 0) {
					return true;
				}
			} while (bytes[offset++] == value[index++]);

			return false;
		} else {
			return false;
		}
	}
	public int indexOf(byte[] text) {
		return indexOf(bytes, 0, bytes.length, text, 0, text.length, cursor);
	}
	public int indexOf(String text) {
		return indexOf(bytes, 0, bytes.length, text.getBytes(), 0, text.length(), cursor);
	}
	public int indexOf(String text, int startFrom) {
		return indexOf(bytes, 0, bytes.length, text.getBytes(), 0, text.length(), startFrom);
	}
	static int indexOf(byte[] array, int startArray, int endArray, byte[] subarray, int startSubarray, int endSubarray, int startFrom) {
		if (startFrom >= endArray) {
			return endSubarray == 0 ? endArray : -1;
		} else {
			if (startFrom < 0) {
				startFrom = 0;
			}

			if (endSubarray == 0) {
				return startFrom;
			} else {
				byte var7 = subarray[startSubarray];
				int var8 = startArray + (endArray - endSubarray);

				for (int var9 = startArray + startFrom; var9 <= var8; ++var9) {
					if (array[var9] != var7) {
						do {
							++var9;
						} while (var9 <= var8 && array[var9] != var7);
					}

					if (var9 <= var8) {
						int startArray0 = var9 + 1;
						int startArray1 = startArray0 + endSubarray - 1;

						for (int startArray2 = startSubarray + 1; startArray0 < startArray1 && array[startArray0] == subarray[startArray2]; ++startArray2) {
							++startArray0;
						}

						if (startArray0 == startArray1) {
							return var9 - startArray;
						}
					}
				}

				return -1;
			}
		}
	}
}
