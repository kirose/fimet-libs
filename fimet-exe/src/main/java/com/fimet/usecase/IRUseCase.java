package com.fimet.usecase;

import java.util.List;
import java.util.Map;
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
	public boolean hasProperty(String key);
	public String getProperty(String key);
	public String getAcquirer();
	public String getName();
	public Map<String, String> getProperties();
	public List<IRSimulator> getSimulators();
}
