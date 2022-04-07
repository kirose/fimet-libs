package com.fimet.exe;

import com.fimet.stress.IStress;

public interface IStressIterator {
	boolean hasNext();
	IStress next();
}
