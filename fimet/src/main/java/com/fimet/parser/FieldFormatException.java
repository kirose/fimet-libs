package com.fimet.parser;

import com.fimet.FimetException;

public class FieldFormatException extends FimetException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public FieldFormatException() {
	}

	public FieldFormatException(String message) {
		super(message);
	}

	public FieldFormatException(Throwable cause) {
		super(cause);
	}

	public FieldFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
