package com.fimet;

public class ManagerException extends FimetException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManagerException() {
	}

	public ManagerException(String message) {
		super(message);
	}

	public ManagerException(Throwable cause) {
		super(cause);
	}

	public ManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
