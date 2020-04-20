package com.fimet.core.usecase.exe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fimet.core.net.ISocket;

public class ExecutionResult {
	private ExecutionStatus status;
	private Map<ISocket, ExecutionSocketResult> path;
	public ExecutionResult() {
		this.path = new LinkedHashMap<ISocket, ExecutionSocketResult>();
	}
	public ExecutionStatus getStatus() {
		return status;
	}
	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}
	public ExecutionSocketResult getOrPutSocketData(ISocket socket) {
		if (!path.containsKey(socket)) {
			path.put(socket, new ExecutionSocketResult());
		}
		return path.get(socket);
	}
	public ExecutionSocketResult getSocketData(ISocket socket) {
		return path.get(socket);
	}
}
