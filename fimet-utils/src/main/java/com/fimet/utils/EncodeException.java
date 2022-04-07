package com.fimet.utils;


public class EncodeException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public EncodeException() {
	}

	public EncodeException(String message) {
		super(message);
	}

	public EncodeException(Throwable cause) {
		super(cause);
	}

	public EncodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EncodeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
