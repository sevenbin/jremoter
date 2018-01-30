/**   
 * @title: LoggerFactory.java 
 * @package io.nix.core.logging 
 * @description: 日志工厂,生成各种类型的日志对象
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 上午10:59:56 
 * @version 1.0.0
 */
package com.jremoter.core.logging;

import com.jremoter.core.logging.support.JdkLoggerFactory;
import com.jremoter.core.logging.support.Log4JLoggerFactory;
import com.jremoter.core.logging.support.Slf4JLoggerFactory;

public abstract class LoggerFactory {
	
	private static volatile LoggerFactory defaultFactory;
	
	private static LoggerFactory createDefaultFactory(String name){
		LoggerFactory loggerFactory;
		try{
			Class.forName("org.slf4j.impl.StaticLoggerBinder");
			loggerFactory = new Slf4JLoggerFactory(true);
			loggerFactory.newInstance(name).debug("using slf4j as the default logging framework");
		}catch(Throwable t){
			try{
				loggerFactory = new Log4JLoggerFactory();
				loggerFactory.newInstance(name).debug("using log4j as the default logging framework");
			}catch(Throwable tt){
				loggerFactory = new JdkLoggerFactory();
				loggerFactory.newInstance(name).debug("using java.util.logging as the default logging framework");
			}
		}
		return loggerFactory;
	}
	
	public static LoggerFactory getDefaultFactory(){
		if(null == defaultFactory){
			defaultFactory = createDefaultFactory(LoggerFactory.class.getName());
		}
		return defaultFactory;
	}
	
	public static void setDefaultFactory(LoggerFactory defaultFactory){
		if(null == defaultFactory){
			throw new NullPointerException("defaultFactory");
		}
		LoggerFactory.defaultFactory = defaultFactory;
	}
	
	public static Logger getLogger(Class<?> requireType){
		return getLogger(requireType.getName());
	}
	
	public static Logger getLogger(String name){
		return getDefaultFactory().newInstance(name);
	}
	
	protected abstract Logger newInstance(String name);
	
}