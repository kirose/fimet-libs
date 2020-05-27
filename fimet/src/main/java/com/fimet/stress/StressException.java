package com.fimet.stress;
/**
 * 
 */

import com.fimet.FimetException;

/**
 * @author Marco Salazar
 *
 */
public class StressException extends FimetException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public StressException() {
	}

	/**
	 * @param message
	 */
	public StressException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StressException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StressException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public StressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
