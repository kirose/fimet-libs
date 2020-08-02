package com.fimet.usecase;

import com.fimet.exe.IStore;
import com.fimet.net.ISocketStore;
import com.fimet.simulator.ISimulatorStore;

public interface IUseCaseStore extends ISimulatorStore, ISocketStore, IStore {
	void storeUseCase(IUseCase useCase);
	void storeProperty(IUseCase useCase, String name, String value);
}
