package com.fimet.simulator;

import com.fimet.parser.IMessage;

public interface ISimulatorField {
	void simulate(IMessage message, String idField);
}
