package com.fimet.assertions;

import com.fimet.parser.IMessage;

public interface IAssertionMaker {
	IAssertion make(IMessage m);
}
