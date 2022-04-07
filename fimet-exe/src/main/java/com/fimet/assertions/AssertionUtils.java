package com.fimet.assertions;

class AssertionUtils {
	public static boolean equals(Object expected, Object value) {
		return (expected!=null && expected.equals(value))||(expected==null&&value==null);		
	}
	public static boolean notEquals(Object expected, Object value) {
		return !equals(expected, value);		
	}
	public static boolean in(Object expected, Object ...values) {
		if (values!=null) {
			for (Object value : values) {
				if (equals(expected, value)) return true;
			}
		}
		return false;
	}
	public static boolean notIn(Object expected, Object ...values) {
		if (values!=null) {
			for (Object value : values) {
				if (equals(expected, value)) return false;
			}
		}
		return true;
	}
	public static boolean isNull(Object value) {
		return value==null;
	}
	public static boolean isNotNull(Object value) {
		return value!=null;
	}
}
