package org.jremoter.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解处理工具类
 * @author koko
 *
 */
public abstract class AnnotationUtil {
	
	private AnnotationUtil(){}
	
	/**
	 * 判断是否存在指定注解
	 * @param requireType
	 * @param annotationClass
	 * @return true存在注解,false不存在,当传入参数为null时,返回false
	 */
	public static boolean hasAnnotation(Class<?> requireType,Class<? extends Annotation> annotationClass){
		if(null == requireType || null == annotationClass){
			return false;
		}
		return requireType.isAnnotationPresent(annotationClass);
	}
	
	public static boolean hasAnnotation(Method requireMethod,Class<? extends Annotation> annotationClass){
		if(null == requireMethod || null == annotationClass){
			return false;
		}
		return requireMethod.isAnnotationPresent(annotationClass);
	}
	
	public static boolean hasAnnotation(Constructor<?> requireConstructor,Class<? extends Annotation> annotationClass){
		if(null == requireConstructor || null == annotationClass){
			return false;
		}
		return requireConstructor.isAnnotationPresent(annotationClass);
	}
	
	public static boolean hasAnnotation(Field requireField,Class<? extends Annotation> annotationClass){
		if(null == requireField || null == annotationClass){
			return false;
		}
		return requireField.isAnnotationPresent(annotationClass);
	}
	
	/**
	 * 判断参数是否包含指定的注解
	 * @param requireMethod
	 * @param annotationClass
	 * @return true存在注解,false不存在,当传入参数为null时,返回false
	 */
	public static boolean hasAnnotationFromParameter(Method requireMethod,Class<? extends Annotation> annotationClass){
		if(null == requireMethod || null == annotationClass){
			return false;
		}
		Annotation[][] annotations = requireMethod.getParameterAnnotations();
		for(Annotation[] ans : annotations){
			for(Annotation an : ans){
				if(an.annotationType().equals(annotationClass)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean hasAnnotationFromParameter(Constructor<?> requireConstructor,Class<? extends Annotation> annotationClass){
		if(null == requireConstructor || null == annotationClass){
			return false;
		}
		Annotation[][] annotations = requireConstructor.getParameterAnnotations();
		for(Annotation[] ans : annotations){
			for(Annotation an : ans){
				if(an.annotationType().equals(annotationClass)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取指定class的注解对象
	 * @param requireType
	 * @param annotationClass
	 * @return null为无此注解
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> requireType,Class<T> annotationClass){
		if(!hasAnnotation(requireType,annotationClass)){
			return null;
		}
		return requireType.getAnnotation(annotationClass);
	}
	
	public static <T extends Annotation> T getAnnotation(Method requireMethod,Class<T> annotationClass){
		if(!hasAnnotation(requireMethod,annotationClass)){
			return null;
		}
		return requireMethod.getAnnotation(annotationClass);
	}
	
	public static <T extends Annotation> T getAnnotation(Field requireField,Class<T> annotationClass){
		if(!hasAnnotation(requireField,annotationClass)){
			return null;
		}
		return requireField.getAnnotation(annotationClass);
	}
	
	public static <T extends Annotation> T getAnnotation(Constructor<?> requireConstructor,Class<T> annotationClass){
		if(!hasAnnotation(requireConstructor,annotationClass)){
			return null;
		}
		return requireConstructor.getAnnotation(annotationClass);
	}
	
	/**
	 * 获取指定类型的注解数组
	 * @param parameterTypes
	 * @param annotations
	 * @param annotation
	 * @return
	 */
	public static Annotation[] getAnnotationFromParameter(Class<?>[] parameterTypes,Annotation[][] annotations,Class<? extends Annotation> annotation){
		if(null == parameterTypes || null == annotations || null == annotation || parameterTypes.length == 0){
			return new Annotation[0];
		}
		Annotation[] list = new Annotation[parameterTypes.length]; 
		for(int i=0;i<parameterTypes.length;i++){
			Annotation[] ans = annotations[i];
			Annotation an = null;
			for(Annotation a : ans){
				if(a.annotationType().equals(annotation)){
					an = a;
					break;
				}
			}
			list[i] = an;
		}
		return list;
	}
	
	public static Annotation[] getAnnotationFromParameter(Method requireMethod,Class<? extends Annotation> annotation){
		if(null == requireMethod || null == annotation){
			return new Annotation[0];
		}
		return getAnnotationFromParameter(requireMethod.getParameterTypes(),requireMethod.getParameterAnnotations(),annotation);
	}
	
	public static Annotation[] getAnnotationFromParameter(Constructor<?> requireConstructor,Class<? extends Annotation> annotation){
		if(null == requireConstructor || null == annotation){
			return new Annotation[0];
		}
		return getAnnotationFromParameter(requireConstructor.getParameterTypes(),requireConstructor.getParameterAnnotations(),annotation);
	}
	
}