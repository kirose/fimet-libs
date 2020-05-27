package com.fimet.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class HttpMessage {
	HttpSocket socket;
	UUID id;
	String url;
	String method;
	String version;
	Map<String, String> headers;
	Map<String, String> params;
	byte[] body;
	Object response;
	HttpCode code = HttpCode.OK;
	public HttpMessage() {
		id = UUID.randomUUID();
		headers = new HashMap<>();
		params = new HashMap<>();
	}
	public HttpSocket getSocket() {
		return socket;
	}
	public void setSocket(HttpSocket socket) {
		this.socket = socket;
	}
	public UUID getId() {
		return id;
	}
	public void addParam(String name, String value) {
		params.put(name, value);
	}
	public String getParam(String name) {
		return params.get(name);
	}
	public String getHeader(String name) {
		return headers.get(name);
	}
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}
	public String getUrl() {
		return url;
	}
	public String getMethod() {
		return method;
	}
	public String getVersion() {
		return version;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public byte[] getBody() {
		return body;
	}
	public HttpCode getCode() {
		return code;
	}
	public void setCode(HttpCode code) {
		this.code = code;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public String toString() {
		StringBuilder s = new StringBuilder("******* Http Message **********\n");
		s.append(method+" "+code+"\n");
		if (!headers.isEmpty()) {
			for (Entry<String, String> e : headers.entrySet()) {
				s.append(e.getKey()+": "+e.getValue()+"\n");
			}
		}
		s.append("\n");
		s.append(body != null ? new String(body) : "");
		s.append("\n");
		return s.toString();
	}
}
