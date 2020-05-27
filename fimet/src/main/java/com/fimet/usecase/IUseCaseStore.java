package com.fimet.usecase;

import java.util.UUID;

import com.fimet.simulator.ISimulatorStore;
import com.fimet.socket.ISocketStore;

public interface IUseCaseStore extends ISimulatorStore, ISocketStore {
	void open(UUID idTask);
	void close(UUID idTask);
	void storeUseCase(IUseCase useCase);
}
