package com.fimet.server;

public enum HttpContentType {
	TEXT_HTML("text/html"),
	TEXT_PLAIN("text/plain"),
	APPLICATION_JSON("application/json"),
	APPLICATION_CSV("application/csv"),
	APPLICATION_XML("application/xml"),
	APPLICATION_OCTET_STREAM("application/octet-stream"),
	APPLICATION_TXT("application/txt"),
	MULTIPART_FORM_DATA("multipart/form-data"),
	;
	String type;
	private HttpContentType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}