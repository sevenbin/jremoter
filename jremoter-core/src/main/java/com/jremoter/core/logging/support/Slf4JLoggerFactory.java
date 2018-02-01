/**   
 * @title: Slf4JLoggerFactory.java 
 * @package io.nix.core.logging.support 
 * @description: slf4j日志工厂
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 上午11:35:26 
 * @version 1.0.0
 */
package com.jremoter.core.logging.support;

import org.slf4j.helpers.NOPLoggerFactory;

import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;

public class Slf4JLoggerFactory extends LoggerFactory{
	
	public Slf4JLoggerFactory(boolean failIfNOP){
		assert failIfNOP;
		if(org.slf4j.LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory){
			throw new NoClassDefFoundError("NOPLoggerFactory not supported");
		}
	}
	
	@Override
	protected Logger newInstance(String name) {
		 return new Slf4JLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
	
}