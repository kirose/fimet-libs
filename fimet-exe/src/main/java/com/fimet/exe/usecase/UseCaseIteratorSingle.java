package com.fimet.exe.usecase;

import java.io.File;
import java.util.Iterator;

import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class UseCaseIteratorSingle implements Iterator<UseCase> {
	private UseCase useCase;
	public UseCaseIteratorSingle(UseCase useCase) {
		this.useCase = useCase;
	}
	public UseCaseIteratorSingle(File file) {
		useCase = (UseCase)UseCaseUtils.fromFile(file);
	}
	@Override
	public boolean hasNext() {
		return useCase!=null;
	}

	@Override
	public UseCase next() {
		UseCase next = useCase;
		useCase = null;
		return next;
	}
}
