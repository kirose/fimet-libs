package com.fimet.simulator;

import com.fimet.FimetLogger;
import com.fimet.assertions.IAssertionMaker;
import com.fimet.assertions.IAssertionResult;
import com.fimet.parser.IMessage;

public abstract class AbstractSimulatorExtension implements ISimulatorExtension {
	protected IAssertionResult exe(String name, IMessage m, IAssertionMaker v) {
		try {
			return v.make(m).execute(name);
		} catch (Exception e) {
			FimetLogger.warning(getClass(), e);
			return null;
		}
	}
}
