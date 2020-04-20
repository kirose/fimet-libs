package com.fimet.core.validator;

import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.ISocket;

public class NullValidator implements IValidator {
	public static final IValidator INSTANCE = new NullValidator();
	@Override
	public void onWriteMessage(ISocket socket, Message msg) {
	}

	@Override
	public void onReadMessage(ISocket socket, Message msg) {
	}


}
