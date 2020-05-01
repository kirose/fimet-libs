package com.fimet.usecase.exe;

import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.IUseCase;

public class NullStoreResults implements IStoreResults {
	public static final IStoreResults INSTANCE = new NullStoreResults();

	@Override
	public void storeStart(IUseCase useCase) {
	}

	@Override
	public void storeIncoming(ISimulator socket, IUseCase useCase, ValidationResult[] validations, Message inMsg,
			byte[] inBytes) {
	}

	@Override
	public void storeOutgoing(ISimulator socket, IUseCase useCase, Message outMsg, byte[] outBytes) {
	}

	@Override
	public void storeFinish(IUseCase useCase, ExecutionResult result) {
	}
	
}
