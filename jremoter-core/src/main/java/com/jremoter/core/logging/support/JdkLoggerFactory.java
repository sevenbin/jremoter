/**   
 * @title: JdkLoggerFactory.java 
 * @package io.nix.core.logging.support 
 * @description: JDK内置LoggerFactory封装
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 上午11:50:38 
 * @version 1.0.0
 */
package com.jremoter.core.logging.support;

import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;

public class JdkLoggerFactory extends LoggerFactory{

	@Override
	protected Logger newInstance(String name) {
		return new JdkLogger(java.util.logging.Logger.getLogger(name));
	}
	
}