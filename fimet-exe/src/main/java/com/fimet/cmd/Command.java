package com.fimet.cmd;

import java.util.Map;
import java.util.UUID;

public class Command {
	public enum Status {
		OK, ERROR
	}
	UUID id;
	String command;
	Map<String, String> params;
	byte[] data;
	public Command() {
		this(null);
	}
	public Command(UUID id) {
		this.id = id!=null?id:UUID.randomUUID();
	}
	public UUID getId() {
		return id;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public boolean hasParam(String param) {
		return params.containsKey(param);
	}
	public String getParam(String param) {
		return params.get(param);
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public byte[] getData() {
		return data;
	}
	public void setDataValue(String data) {
		this.data = data!=null?data.getBytes():null;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
