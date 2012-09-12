package com.kissme.core.orm;

/**
 * 
 * @author loudyn
 * 
 */
public class DataAccessException extends RuntimeException {

	/**
	 * 
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
