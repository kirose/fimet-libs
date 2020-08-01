package com.fimet.exe.stress;

import java.io.File;
import java.util.Iterator;

import com.fimet.stress.Stress;
import com.fimet.utils.StressUtils;

public class StressIteratorSingle implements Iterator<Stress> {
	Stress stress;
	public StressIteratorSingle(Stress stress) {
		this.stress = stress;
	}
	public StressIteratorSingle(File file) {
		stress = (Stress)StressUtils.fromFile(file);
	}
	@Override
	public boolean hasNext() {
		return stress!=null;
	}

	@Override
	public Stress next() {
		Stress next = stress;
		stress = null;
		return next;
	}
}
