package com.jremoter.core.exception;

public class BeanConstructorSelectorException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BeanConstructorSelectorException() {
		super();
	}

	public BeanConstructorSelectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanConstructorSelectorException(String message) {
		super(message);
	}

	public BeanConstructorSelectorException(Throwable cause) {
		super(cause);
	}
	
}