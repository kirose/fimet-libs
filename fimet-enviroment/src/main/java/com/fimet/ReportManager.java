package com.fimet;

import java.io.File;
import java.util.List;

import com.fimet.report.IUseCaseReport;
import com.fimet.report.ReportException;
import com.fimet.report.UseCaseReportXLSX;
import com.fimet.usecase.IRUseCase;

public class ReportManager implements IReportManager {

	public File doUseCaseReport(File folderOutput, List<IRUseCase> useCases, String type) {
		IUseCaseReport report = newUseCaseReport(type);
		String extension = report.getExtension();
		File path = new File(folderOutput, "UseCaseReport."+extension);
		return report.doReport(path, useCases);
	}
	public File doUseCaseReport(File folderOutput, String idTask, String type) {
		IUseCaseReport report = newUseCaseReport(type);
		String extension = report.getExtension();
		File path = new File(folderOutput, "UseCaseReport."+extension);
		return report.doReport(path, null);
	}
	private IUseCaseReport newUseCaseReport(String type) {
		if ("xlsx".equalsIgnoreCase(type)) {
			return new UseCaseReportXLSX();
		}
		throw new ReportException("Invalid type report "+type);
	}
	public File doStressReport(File folderOutput, String idTask, String type) {
		throw new ReportException("Invalid type report "+type);
	}

	public void start() {
	}

	public void reload() {
	}


}
