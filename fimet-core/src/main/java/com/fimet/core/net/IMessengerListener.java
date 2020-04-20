package com.fimet.core.net;

import com.fimet.core.iso8583.parser.Message;

public interface IMessengerListener {
	void onMessengerConnected(IMessenger messenger);
	void onMessengerConnecting(IMessenger messenger);
	void onMessengerDisconnected(IMessenger messenger);
	void onMessengerWriteToSocket(IMessenger messenger, byte[] message);
	void onMessengerWriteMessage(IMessenger messenger, Message message);
	void onMessengerReadFromSocket(IMessenger messenger, byte[] message);
	void onMessengerReadMessage(IMessenger messenger, Message message);
}
