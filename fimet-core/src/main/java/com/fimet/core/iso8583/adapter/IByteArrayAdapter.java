package com.fimet.core.iso8583.adapter;

public interface IByteArrayAdapter extends IAdapter {
	boolean isAdaptable(byte[] message);
	byte[] readByteArray(byte[] message);
	byte[] writeByteArray(byte[] message);
}
