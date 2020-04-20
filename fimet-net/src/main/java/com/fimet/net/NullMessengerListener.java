package com.fimet.net;

import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerListener;

public class NullMessengerListener implements IMessengerListener {

	@Override
	public void onMessengerConnected(IMessenger messenger) {
	}

	@Override
	public void onMessengerConnecting(IMessenger messenger) {
	}

	@Override
	public void onMessengerDisconnected(IMessenger messenger) {
	}

	@Override
	public void onMessengerWriteToSocket(IMessenger messenger, byte[] message) {
	}

	@Override
	public void onMessengerWriteMessage(IMessenger messenger, Message message) {
	}

	@Override
	public void onMessengerReadFromSocket(IMessenger messenger, byte[] message) {
	}

	@Override
	public void onMessengerReadMessage(IMessenger messenger, Message message) {
	}



}
