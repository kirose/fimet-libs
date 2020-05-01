package com.fimet.stress.creator;

import com.fimet.iso8583.parser.Message;

public interface IVariator {
	Message variate(Message message, FieldVariation[] variation);
}
