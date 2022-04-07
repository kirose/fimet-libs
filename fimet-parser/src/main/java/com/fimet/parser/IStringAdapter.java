package com.fimet.parser;

public interface IStringAdapter extends IAdapter {
	boolean isAdaptable(String message);
	byte[] readString(String message);
	String writeString(byte[] message);
}
