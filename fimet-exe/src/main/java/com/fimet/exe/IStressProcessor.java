package com.fimet.exe;

import com.fimet.IExtension;
import com.fimet.stress.IStress;

public interface IStressProcessor extends IExtension {
	void process(StressTask executing, IStress useCase);
}
