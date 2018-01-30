package com.jremoter.core.util;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


/**
 * class处理工具类
 * @author koko
 *
 */
public abstract class ClassUtil {
	
	private ClassUtil(){}
	
	private static final String ARRAY_SUFFIX = "[]";
	private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
	private static final String INTERNAL_ARRAY_PREFIX = "[";
	private static final char PACKAGE_SEPARATOR = '.';
	private static final char INNER_CLASS_SEPARATOR = '$';
	private static final Map<String,Class<?>> classCache = new HashMap<String,Class<?>>(32);
	
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
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String name,ClassLoader classLoader){
		if(StringUtil.isBlank(name)){
			return null;
		}
		Class<T> result = (Class<T>)classCache.get(name);
		if(null != result){
			return result;
		}
		//"java.lang.String[]" style arrays
		if(name.endsWith(ARRAY_SUFFIX)){
			String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			Class<?> elementClass = forName(elementClassName, classLoader);
			return (Class<T>)Array.newInstance(elementClass, 0).getClass();
		}
		//"[Ljava.lang.String;" style arrays
		if(name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")){
			String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
			Class<?> elementClass = forName(elementName, classLoader);
			return (Class<T>)Array.newInstance(elementClass, 0).getClass();
		}
		//"[[I" or "[[Ljava.lang.String;" style arrays
		if(name.startsWith(INTERNAL_ARRAY_PREFIX)){
			String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
			Class<?> elementClass = forName(elementName, classLoader);
			return (Class<T>)Array.newInstance(elementClass, 0).getClass();
		}
		ClassLoader tempClassLoader = classLoader;
		if(null == tempClassLoader){
			tempClassLoader = getDefaultClassLoader();
		}
		try{
			return (Class<T>)(tempClassLoader != null ? tempClassLoader.loadClass(name) : Class.forName(name));
		}catch(ClassNotFoundException e){
			int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
			if(lastDotIndex != -1){
				String innerClassName = name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
				try{
					return (Class<T>)(tempClassLoader != null ? tempClassLoader.loadClass(innerClassName) : Class.forName(innerClassName));
				}catch(ClassNotFoundException ee){
					
				}
			}
		}
		return null;
	}
	
}