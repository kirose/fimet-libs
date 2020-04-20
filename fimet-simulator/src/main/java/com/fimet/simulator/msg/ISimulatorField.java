package com.fimet.simulator.msg;

import com.fimet.core.iso8583.parser.Message;

public interface ISimulatorField {
	void simulate(Message message, String idField);
}
