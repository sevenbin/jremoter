package com.jremoter.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jremoter.core.toolkit.ReferenceHashMap;

/**
 * 反射常用工具类
 * @author koko
 *
 */
public abstract class ReflectionUtil {
	
	private ReflectionUtil(){}
	
	//空对象
	private static final Method[] 		EMPTY_METHODS 	= {};
	private static final Field[] 		EMPTY_FIELDS 	= {};
	private static final Class<?>[] 	EMPTY_CLASSES 	= {};
	//缓存
	private static final Map<Class<?>,Method[]> cacheMethods = new ReferenceHashMap<Class<?>,Method[]>(256);
	private static final Map<Class<?>,Field[]> cacheFields = new ReferenceHashMap<Class<?>,Field[]>(256);
	private static final Map<Class<?>,Class<?>[]> cacheInterfaces = new ReferenceHashMap<Class<?>,Class<?>[]>(256);
	
	/**
	 * 获取指定class对应的接口,使用缓存
	 * @param requireType
	 * @return 返回接口数组
	 */
	public static Class<?>[] getInterface(Class<?> requireType){
		if(null == requireType){
			return EMPTY_CLASSES;
		}
		Class<?>[] result = cacheInterfaces.get(requireType);
		if(null == result){
			result = requireType.getInterfaces();
			cacheInterfaces.put(requireType,(result.length == 0 ? EMPTY_CLASSES : result));
		}
		return result;
	}
	
	/**
	 * 获取指定class生命的field,使用缓存
	 * @param requireType
	 * @return 返回Field数组
	 */
	public static Field[] getDeclaredFields(Class<?> requireType){
		if(null == requireType){
			return EMPTY_FIELDS;
		}
		Field[] result = cacheFields.get(requireType);
		if(null == result){
			result = requireType.getDeclaredFields();
			cacheFields.put(requireType,(result.length == 0 ? EMPTY_FIELDS : result));
		}
		return result;
	}
	
	/**
	 * 获取指定class的method,使用缓存
	 * @param requireType
	 * @return 返回Method数组
	 */
	public static Method[] getDeclaredMethods(Class<?> requireType){
		if(null == requireType){
			return EMPTY_METHODS;
		}
		Method[] result = cacheMethods.get(requireType);
		if(null == result){
			Method[] declaredMethods = requireType.getDeclaredMethods();
			List<Method> defaultMethods = findMethodsOnInterfaces(requireType);
			if(null != defaultMethods && defaultMethods.size() > 0){
				result = new Method[declaredMethods.length + defaultMethods.size()];
				System.arraycopy(declaredMethods,0,result,0,declaredMethods.length);
				int index = declaredMethods.length;
				for(Method defaultMethod : defaultMethods){
					result[index] = defaultMethod;
					index++;
				}
			}else{
				result = declaredMethods;
			}
			cacheMethods.put(requireType,(result.length == 0 ? EMPTY_METHODS : result));
		}
		return result;
	}
	
	/**
	 * 调用构造函数
	 * @param constructor
	 * @return 返回调用构造函数后创建的实例
	 */
	public static Object invokeConstructor(Constructor<?> constructor){
		return invokeConstructor(constructor,new Object[0]);
	}
	
	/**
	 * 调用构造函数
	 * @param constructor
	 * @param args
	 * @return 返回调用构造函数后创建的实例
	 */
	public static Object invokeConstructor(Constructor<?> constructor,Object...args){
		try{
			return constructor.newInstance(args);
		}catch(Exception e){
			handleReflectionException(e);
		}
		throw new IllegalStateException("should never get here");
	}
	
	/**
	 * 通过反射调用指定方法,可自定义参数
	 * @param method
	 * @param target
	 * @param args
	 * @return 返回调用方法后创建的实例
	 */
	public static Object invokeMethod(Method method,Object target,Object...args){
		try{
			return method.invoke(target,args);
		}catch(Exception e){
			handleReflectionException(e);
		}
		throw new IllegalStateException("should never get here");
	}
	
	/**
	 * 通过反射调用指定方法,无参
	 * @param method
	 * @param target
	 * @return 返回调用方法后创建的实例
	 */
	public static Object invokeMethod(Method method,Object target){
		return invokeMethod(method, target, new Object[0]);
	}
	
	/**
	 * 修改方法的访问级别
	 * @param method
	 */
	public static void makeAccessible(Method method){
		if(null == method){
			return;
		}
		if((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()){
			method.setAccessible(true);
		}
	}
	
	/**
	 * 修改构造函数的访问级别
	 * @param constructor
	 */
	public static void makeAccessible(Constructor<?> constructor){
		if(null == constructor){
			return;
		}
		if((!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) && !constructor.isAccessible()){
			constructor.setAccessible(true);
		}
	}
	
	/**
	 * 修改属性的访问级别
	 * @param field
	 */
	public static void makeAccessible(Field field){
		if(null == field){
			return;
		}
		if((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()){
			field.setAccessible(true);
		}
	}
	
	/**
	 * 判断方法是否Object的方法
	 * @param method
	 * @return 返回true为Object内含有的方法,false为Object不含有的方法
	 */
	public static boolean isObjectMethod(Method method){
		if(null == method){
			return false;
		}
		try{
			Object.class.getDeclaredMethod(method.getName(),method.getParameterTypes());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private static void handleReflectionException(Exception e){
		if(e instanceof NoSuchMethodException){
			throw new IllegalStateException("method not found : " + e.getMessage());
		}
		if(e instanceof IllegalAccessException){
			throw new IllegalStateException("could not access method : " + e.getMessage());
		}
		if(e instanceof InvocationTargetException){
			handleInvocationTargetException((InvocationTargetException)e);
		}
		if(e instanceof RuntimeException){
			throw (RuntimeException)e;
		}
		throw new UndeclaredThrowableException(e);
	}
	
	private static void handleInvocationTargetException(InvocationTargetException e){
		rethrowRuntimeException(e.getTargetException());
	}
	private static void rethrowRuntimeException(Throwable e){
		if(e instanceof RuntimeException){
			throw (RuntimeException)e;
		}
		if(e instanceof Error){
			throw (Error)e;
		}
		throw new UndeclaredThrowableException(e);
	}
	
	/**
	 * 查找接口方法
	 * @param requireType
	 * @return 返回方法列表
	 */
	private static List<Method> findMethodsOnInterfaces(Class<?> requireType){
		List<Method> result = new LinkedList<Method>();
		for(Class<?> interfaceClass : requireType.getInterfaces()){
			for(Method interfaceMethod : interfaceClass.getMethods()){
				result.add(interfaceMethod);
			}
		}
		return result;
	}
	
}