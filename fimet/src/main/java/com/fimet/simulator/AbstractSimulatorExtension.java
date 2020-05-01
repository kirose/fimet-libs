package com.fimet.simulator;

import com.fimet.ISessionManager;
import com.fimet.Manager;
import com.fimet.iso8583.parser.Message;
import com.fimet.usecase.Session;

public abstract class AbstractSimulatorExtension implements ISimulatorExtension {
	static ISessionManager sessionManager = Manager.get(ISessionManager.class);
	protected boolean exe(IValidator v) {
		return v.validate();
	}
	protected int indexOf(ISimulator s, Message m) {
		if (m != null) {
			Session session = sessionManager.getSession(m);
			if (session != null && session.getUseCase() != null) {
				return session.getUseCase().getSimulators().indexOf(s);
			}
		}
		return -1;
	}

}
