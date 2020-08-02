package com.fimet;

import java.io.File;
import java.util.List;

import com.fimet.usecase.IRUseCase;

public interface IReportManager extends IManager {
	/**
	 * 
	 * @param folderOutput
	 * @param idTask the task id associated
	 * @param type is the extension of the report (UseCaseReport.type)
	 * @return File report (UseCaseReport.type)
	 */
	File doUseCaseReport(File folderOutput, String idTask, String type);
	/**
	 * 
	 * @param idTask the task id associated
	 * @param type is the extension of the report (UseCaseReport.type)
	 * @return File report (UseCaseReport.type)
	 */
	File doUseCaseReport(File folderOutput, List<? extends IRUseCase> useCases, String type);
	/**
	 * 
	 * @param idTask the task id associated
	 * @param type is the extension of the report (StressReport.type)
	 * @return File report (StressReport.type)
	 */
	File doStressReport(File folderOutput, String idTask, String type);
}
