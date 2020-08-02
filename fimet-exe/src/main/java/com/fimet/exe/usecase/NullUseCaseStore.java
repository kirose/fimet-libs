package com.fimet.exe.usecase;

import com.fimet.exe.Task;
import com.fimet.net.ISocket;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseStore;

public class NullUseCaseStore implements IUseCaseStore {
	public static final IUseCaseStore INSTANCE = new NullUseCaseStore();
	@Override
	public void init(Task task, Object ...params) {}
	@Override
	public void storeUseCase(IUseCase useCase) {}
	@Override
	public void storeProperty(IUseCase useCase, String name, String value) {}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {}
	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {}
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {}
	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {}
	@Override
	public void save() {}
	@Override
	public void close() {}
}
