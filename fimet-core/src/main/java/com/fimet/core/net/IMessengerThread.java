package com.fimet.core.net;

import com.fimet.core.iso8583.parser.Message;

public interface IMessengerThread {
	void onSocketRead(IMessenger messenger, byte[] bytes);
	void onMessengerWrite(IMessenger messenger, Message msg);
}
