/**   
 * @title: BeanDefinitionExistException.java 
 * @package io.nix.core.exception 
 * @description: Bean定义已经存在异常
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 下午2:39:31 
 * @version 1.0.0
 */
package com.jremoter.core.exception;

public class BeanDefinitionExistException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BeanDefinitionExistException() {
		super();
	}

	public BeanDefinitionExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionExistException(String message) {
		super(message);
	}

	public BeanDefinitionExistException(Throwable cause) {
		super(cause);
	}
	
}