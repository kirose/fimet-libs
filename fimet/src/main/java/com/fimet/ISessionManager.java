package com.fimet;

import com.fimet.iso8583.parser.Message;
import com.fimet.usecase.Session;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.exe.ISessionListener;

public interface ISessionManager extends IManager {
	Session getSession(Message message);
	Session createSession(UseCase useCase);
	Session createSession(UseCase useCase, ISessionListener listener);
	void deleteSession(UseCase useCase);
}
