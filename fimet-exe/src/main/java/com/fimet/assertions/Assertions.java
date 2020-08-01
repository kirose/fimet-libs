package com.fimet.assertions;


public final class Assertions {
	public static Equals Equals(Object value, Object expected) {
		return new Equals(value, expected);
	}
	public static NotEquals NotEquals(Object value, Object expected) {
		return new NotEquals(value, expected);
	}
	public static In In(Object value, Object ...expected) {
		return new In(value, expected);
	}
	public static NotIn NotIn(Object value, Object ... expected) {
		return new NotIn(value, expected);
	}
	public static IsNull IsNull(Object value) {
		return new IsNull(value);
	}
	public static IsNotNull IsNotNull(Object value) {
		return new IsNotNull(value);
	}
	public static IsTrue IsTrue(boolean value) {
		return new IsTrue(value);
	}
}
