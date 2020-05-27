package com.fimet.exe.usecase;

import java.util.UUID;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.ISocket;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseStore;

public class NullUseCaseStore implements IUseCaseStore {
	public static final IUseCaseStore INSTANCE = new NullUseCaseStore();
	@Override
	public void storeUseCase(IUseCase useCase) {}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {}
	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {}
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {}
	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {}
	@Override
	public void open(UUID idTask) {}
	@Override
	public void close(UUID idTask) {}

}
