package com.fimet.exe.stress;

import java.io.File;
import java.util.Iterator;

import com.fimet.stress.Stress;
import com.fimet.utils.StressUtils;

public class StressIteratorSingle implements Iterator<Stress> {
	Stress stress;
	File folder;
	public StressIteratorSingle(Stress stress, File folder) {
		this.folder = folder;
		this.stress = stress;
	}
	public StressIteratorSingle(File file, File folder) {
		this.folder = folder;
		stress = (Stress)StressUtils.fromFile(file, folder);
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
