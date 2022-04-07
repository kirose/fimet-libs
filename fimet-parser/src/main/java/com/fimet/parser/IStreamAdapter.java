package com.fimet.parser;

import java.io.InputStream;
import java.io.OutputStream;

public interface IStreamAdapter extends IAdapter {
	boolean isAdaptable(InputStream in);
	byte[] readStream(InputStream in);
	void writeStream(OutputStream out, byte[] message);
}
