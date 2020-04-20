package com.fimet.simulator.msg;

import com.fimet.core.iso8583.parser.Message;

public abstract class SimulatorField {
	abstract public void simulate(Message message);
}
