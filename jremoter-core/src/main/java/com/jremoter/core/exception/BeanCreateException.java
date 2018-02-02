package com.jremoter.core.exception;

public class BeanCreateException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BeanCreateException() {
		super();
	}

	public BeanCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanCreateException(String message) {
		super(message);
	}

	public BeanCreateException(Throwable cause) {
		super(cause);
	}
	
}