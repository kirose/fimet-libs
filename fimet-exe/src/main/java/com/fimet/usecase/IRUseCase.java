package com.fimet.usecase;

import java.util.List;
/**
 * Interface for a report entity of usecases 
 * @author msalazar
 *
 */
public interface IRUseCase {
	public String getStatus();
	public Long getStartTime();
	public Long getExecutionTime();
	public String getResponseCode();
	public String getAuthorizationCode();
	public String getAcquirer();
	public String getName();
	public List<IRSimulator> getSimulators();
}
