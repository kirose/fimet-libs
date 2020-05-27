package com.fimet.stress.creator;

import com.fimet.parser.IMessage;

public interface IVariator {
	IMessage variate(IMessage message, FieldVariation[] variation);
}
