package com.fimet.usecase.exe;

import com.fimet.usecase.UseCase;

public interface ISessionListener {
	void onSessionExpire(UseCase useCase);
}
