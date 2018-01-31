/**   
 * @title: BeanDefinitionNotFoundException.java 
 * @package io.nix.core.exception 
 * @description: Bean定义未找到异常
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 下午1:58:13 
 * @version 1.0.0
 */
package com.jremoter.core.exception;

public class BeanDefinitionNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BeanDefinitionNotFoundException() {
		super();
	}

	public BeanDefinitionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionNotFoundException(String message) {
		super(message);
	}

	public BeanDefinitionNotFoundException(Throwable cause) {
		super(cause);
	}
	
}