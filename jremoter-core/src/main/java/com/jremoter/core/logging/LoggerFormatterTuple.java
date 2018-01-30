/**   
 * @title: LoggerFormattingTuple.java 
 * @package com.jremoter.basic.logging 
 * @description: log格式化元素类
 * @author TangBin tb_bin@163.com
 * @date 2018年1月19日 上午10:29:10 
 * @version 1.0.0
 */
package com.jremoter.core.logging;

public class LoggerFormatterTuple {
	
	private final String message;
	private final Throwable throwable;
	
	public LoggerFormatterTuple(String message,Throwable throwable){
		this.message = message;
		this.throwable = throwable;
	}
	
	public String getMessage() {
		return message;
	}
	public Throwable getThrowable() {
		return throwable;
	}
	
}