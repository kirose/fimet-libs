package com.fimet.core.stress.creator;

import com.fimet.core.iso8583.parser.Message;

public interface IVariator {
	Message variate(Message message, FieldVariation[] variation);
}
