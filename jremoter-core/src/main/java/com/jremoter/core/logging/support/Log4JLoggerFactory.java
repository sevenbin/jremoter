/**   
 * @title: Log4JLoggerFactory.java 
 * @package io.nix.core.logging.support 
 * @description: LogFactory for log4j
 * @author TangBin tb_bin@163.com
 * @date 2018年1月16日 上午11:25:59 
 * @version 1.0.0
 */
package com.jremoter.core.logging.support;

import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;

public class Log4JLoggerFactory extends LoggerFactory{

	@Override
	protected Logger newInstance(String name) {
		return new Log4JLogger(org.apache.log4j.Logger.getLogger(name));
	}
	
}