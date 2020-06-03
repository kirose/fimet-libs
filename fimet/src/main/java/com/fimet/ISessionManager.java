package com.fimet;

import com.fimet.parser.IMessage;
import com.fimet.usecase.ISessionListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.Session;

public interface ISessionManager extends IManager {
	boolean hasSession(IMessage message);
	Session getSession(IMessage message);
	Session createSession(IUseCase useCase);
	Session createSession(IUseCase useCase, ISessionListener listener);
	void deleteSession(IUseCase useCase);
}
