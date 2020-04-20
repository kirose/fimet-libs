package com.fimet.core.usecase;

import com.fimet.core.IExecutorManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.ISocket;
import com.fimet.core.usecase.exe.ExecutionResult;
import com.fimet.core.usecase.exe.IExecutor;
import com.fimet.core.usecase.exe.IExecutorMonitor;
import com.fimet.core.validator.IValidator;
import com.fimet.core.validator.NullValidator;

public class TestBuilder implements IExecutorMonitor {
	UseCase useCase;
	IExecutor executor;
	public TestBuilder(String name, ISocket acquirer) {
		useCase = new UseCase();
		useCase.setName(name);
		useCase.addConnection(acquirer);
		useCase.setMessage(new Message("0200","ISO858300000",acquirer.getParser()));
		executor = Manager.get(IExecutorManager.class).getSyncExecutor();
		
	}
	public TestBuilder setMti(String mti) {
		useCase.getMessage().setMti(mti);
		return this;
	}
	public TestBuilder setHeader(String header) {
		useCase.getMessage().setHeader(header);
		return this;
	}
	public TestBuilder setMessage(Message message) {
		useCase.setMessage(message);
		return this;
	}
	public TestBuilder setValue(int idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public TestBuilder setValue(String idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public TestBuilder removeValue(int idField) {
		useCase.getMessage().remove(idField);
		useCase.getMessage().remove(idField);
		return this;
	}
	public TestBuilder removeValue(String idField) {
		useCase.getMessage().remove(idField);
		return this;
	}
	public TestBuilder addConnection(ISocket socket) {
		useCase.addConnection(socket);
		return this;
	}
	public TestBuilder setValidator(IValidator validator) {
		useCase.setValidator(validator != null ? validator : NullValidator.INSTANCE);
		return this;
	}
	public TestBuilder execute() {
		executor.setMonitor(this);
		executor.execute(useCase);
		return this;
	}
	@Override
	public void onStart(IUseCase useCase) {
		System.out.println("Start execution "+useCase);
	}
	@Override
	public void onFinish(IUseCase useCase, ExecutionResult data) {
		System.out.println("Finish execution "+useCase);
		executor.removeMonitor();
	}
}
