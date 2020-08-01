package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.usecase.IRUseCase;

public interface IRUseCaseDAO extends IManager {
	List<IRUseCase> getByIdTask(String idTask);
	IRUseCase insert(IRUseCase useCase);
	IRUseCase update(IRUseCase useCase);
	IRUseCase delete(IRUseCase useCase);
}
