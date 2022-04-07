package com.fimet.assertions;


public final class Assertions {
	public static Equals Equals(Object expected, Object value) {
		return new Equals(expected, value);
	}
	public static NotEquals NotEquals(Object expected, Object value) {
		return new NotEquals(expected, value);
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
