package com.fimet.usecase.exe;

import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.IUseCase;

public interface IStoreResults {
	void storeStart(IUseCase useCase);
	void storeIncoming(ISimulator socket, IUseCase useCase, ValidationResult[] validations, Message inMsg, byte[] inBytes);
	void storeOutgoing(ISimulator socket, IUseCase useCase, Message outMsg, byte[] outBytes);
	void storeFinish(IUseCase useCase, ExecutionResult result);
}
