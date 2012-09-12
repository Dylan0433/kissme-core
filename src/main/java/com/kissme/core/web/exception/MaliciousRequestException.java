package com.kissme.core.web.exception;

/**
 * 
 * @author loudyn
 * 
 */
@SuppressWarnings("serial")
public class MaliciousRequestException extends RuntimeException {

	public MaliciousRequestException(){
		super();
	}
	
	public MaliciousRequestException(String message) {
		super(message);
	}
}
