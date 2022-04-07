package com.fimet.simulator;


import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.assertions.IAssertionMaker;
import com.fimet.assertions.IAssertionResult;
import com.fimet.parser.IMessage;

public abstract class AbstractSimulatorExtension implements ISimulatorExtension {
	private static Logger logger = LoggerFactory.getLogger(AbstractSimulatorExtension.class);
	protected IAssertionResult exe(String name, IMessage m, IAssertionMaker v) {
		try {
			return v.make(m).execute(name);
		} catch (Exception e) {
			logger.warn("Abstract Simulator Extension exception",e);
			return null;
		}
	}
}
