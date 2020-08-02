package com.fimet.report;

import java.io.File;
import java.util.List;

import com.fimet.usecase.IRUseCase;

public interface IUseCaseReport {
	String getExtension();
	File doReport(File output, List<? extends IRUseCase> useCases);
}
