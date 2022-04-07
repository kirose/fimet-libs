package com.fimet.assertions;

public class NotIn extends AbstractAssertion {
	
	private Object[] expected;
	
	public NotIn(Object value, Object ... expected) {
		super();
		this.value = value;
		this.expected = expected;
	}
	@Override
	protected boolean doExecute() {
		return AssertionUtils.notIn(value, expected);
	}
	public String toString() {
		StringBuilder s = status().append("[NOTIN]").append("V:[").append(value).append("]");
		s.append("E:[");
		if (expected!=null) {
			for (Object v : expected) {
				s.append(v).append(",");
			}
			s.setLength(s.length()-1);
		}
		s.append("]");
		return s.toString();
	}
	public Object getExpected() {
		return expected;
	}
}
