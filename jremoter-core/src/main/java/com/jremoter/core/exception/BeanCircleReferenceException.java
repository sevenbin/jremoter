/**   
 * @title: BeanReferenceHasCircleException.java 
 * @package io.nix.core.exception 
 * @description: bean引用出现环形引用异常
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 下午2:55:40 
 * @version 1.0.0
 */
package com.jremoter.core.exception;

public class BeanCircleReferenceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public BeanCircleReferenceException() {
		super();
	}

	public BeanCircleReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanCircleReferenceException(String message) {
		super(message);
	}

	public BeanCircleReferenceException(Throwable cause) {
		super(cause);
	}
	
}