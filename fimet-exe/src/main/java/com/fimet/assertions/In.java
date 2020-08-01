package com.fimet.assertions;

public class In extends AbstractAssertion {
	
	private Object[] expected;
	
	public In(Object value, Object ... expected) {
		super();
		this.value = value;
		this.expected = expected;
	}
	@Override
	protected boolean doExecute() {
		return AssertionUtils.in(value, expected);
	}
	public String toString() {
		StringBuilder s = status();
		s.append("V:[").append(value).append("]");
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
