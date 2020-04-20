package com.fimet.core.stress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fimet.core.net.ISocket;

public class Stress implements IStress {
	private String name;
	private Map<ISocket, File> stressFiles;
	public Stress() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<ISocket, File> getStressFiles() {
		return stressFiles;
	}
	public void setStressFiles(Map<ISocket, File> stressFiles) {
		this.stressFiles = stressFiles;
	}
	public File getStressFile(ISocket socket) {
		return stressFiles.get(socket);
	}
	public List<ISocket> getConnections(){
		List<ISocket> connecitons = new ArrayList<>(stressFiles.size());
		Set<Entry<ISocket, File>> entrySet = stressFiles.entrySet();
		for (Entry<ISocket, File> e : entrySet) {
			connecitons.add(e.getKey());
		}
		return connecitons;
	}
}
