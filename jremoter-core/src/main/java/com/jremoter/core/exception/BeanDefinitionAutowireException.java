/**   
 * @title: InjectBeanDefinitionException.java 
 * @package io.nix.core.exception 
 * @description: Bean注入失败异常
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 下午2:25:21 
 * @version 1.0.0
 */
package com.jremoter.core.exception;

public class BeanDefinitionAutowireException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BeanDefinitionAutowireException() {
		super();
	}

	public BeanDefinitionAutowireException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionAutowireException(String message) {
		super(message);
	}

	public BeanDefinitionAutowireException(Throwable cause) {
		super(cause);
	}
	
}