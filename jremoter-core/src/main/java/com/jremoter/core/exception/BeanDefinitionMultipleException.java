/**   
 * @title: MultipleBeanDefinitionException.java 
 * @package io.nix.core.exception 
 * @description: 指定搜索信息存在多个bean定义异常
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 下午3:47:14 
 * @version 1.0.0
 */
package com.jremoter.core.exception;

public class BeanDefinitionMultipleException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BeanDefinitionMultipleException() {
		super();
	}

	public BeanDefinitionMultipleException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDefinitionMultipleException(String message) {
		super(message);
	}

	public BeanDefinitionMultipleException(Throwable cause) {
		super(cause);
	}
	
}