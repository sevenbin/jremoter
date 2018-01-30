package com.jremoter.core.util;

/**
 * class处理工具类
 * @author koko
 *
 */
public abstract class ClassUtil {
	
	private ClassUtil(){}
	
	/**
	 * 获取默认的ClassLoader对象
	 * @return 返回ClassLoader
	 */
	public static ClassLoader getDefaultClassLoader(){
		ClassLoader classLoader = null;
		try{
			classLoader = Thread.currentThread().getContextClassLoader();
		}catch(Exception e){}
		
		if(null == classLoader){
			classLoader = ClassUtil.class.getClassLoader();
			if(null == classLoader){
				try{
					classLoader = ClassLoader.getSystemClassLoader();
				}catch(Exception e){}
			}
		}
		return classLoader;
	}
	
	/**
	 * 获取指定对象的simpleClassName,为null,返回[null_object]
	 * @param requireObject
	 * @return 返回class的SimpleClassName,空返回[null_object]
	 */
	public static String getSimpleClassName(Object requireObject){
		if(null == requireObject){
			return "null_object";
		}else{
			return getSimpleClassName(requireObject.getClass());
		}
	}
	
	/**
	 * 获取指定class的simpleClassName,为null,抛出NullPointerException
	 * @param requireType
	 * @return 返回class的SimpleClassName,空抛出异常
	 */
	public static String getSimpleClassName(Class<?> requireType){
		if(null == requireType){
			throw new NullPointerException("requireType");
		}
		return requireType.getSimpleName();
	}
	
	/**
	 * 获取类名的骆驼命名法
	 * @param requireType
	 * @return 返回类名的骆驼命名法
	 */
	public static String getCamelClassName(Class<?> requireType){
		String simpleClassName = getSimpleClassName(requireType);
		return simpleClassName.substring(0,1).toLowerCase() + simpleClassName.substring(1);
	}
	
}