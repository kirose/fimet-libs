package com.fimet.exe.usecase;

import java.util.Iterator;


import com.fimet.usecase.UseCase;

public class UseCaseIteratorMulti implements Iterator<UseCase> {
	//private static Logger logger = LoggerFactory.getLogger(UseCaseIteratorMultiple.class);
	Iterator<UseCase> useCases;
	UseCase next = null;
	int index = -1;
	public UseCaseIteratorMulti(Iterator<UseCase> useCases) {
		this.useCases = useCases;
	}
	@Override
	public UseCase next() {
		return useCases.next();
	}
	@Override
	public boolean hasNext() {
		return useCases.hasNext();
	}
}
