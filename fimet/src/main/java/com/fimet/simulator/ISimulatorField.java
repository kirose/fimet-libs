package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public interface ISimulatorField {
	void simulate(Message message, String idField);
}
