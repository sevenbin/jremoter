package com.jremoter.core.util;

/**
 * 泛型处理工具类
 * @author koko
 *
 */
public abstract class GenericTypeUtil {
	
	private GenericTypeUtil(){}
	
	/**
	 * 泛型类型转换,避免直接转换时出现unchecked 
	 * @param object
	 * @return 返回泛型类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseType(Object object){
		return (T)object;
	}
	
}