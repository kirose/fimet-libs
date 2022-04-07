package com.fimet.json;

import java.util.List;
import java.util.Map;

public class JStressFileBuilder {
	private JMessageMap message;
	private Map<String,String[]> cartesian;
	private List<Map<String, String>> replacements;
	private JVariator variator;
	public JMessageMap getMessage() {
		return message;
	}
	public void setMessage(JMessageMap message) {
		this.message = message;
	}
	public Map<String, String[]> getCartesian() {
		return cartesian;
	}
	public void setCartesian(Map<String, String[]> cartesian) {
		this.cartesian = cartesian;
	}
	public List<Map<String, String>> getReplacements() {
		return replacements;
	}
	public void setReplacements(List<Map<String, String>> replacements) {
		this.replacements = replacements;
	}
	public JVariator getVariator() {
		return variator;
	}
	public void setVariator(JVariator variator) {
		this.variator = variator;
	}
}
