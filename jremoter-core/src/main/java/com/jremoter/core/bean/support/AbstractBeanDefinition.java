package com.jremoter.core.bean.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.DestoryMethod;
import com.jremoter.core.annotation.InitialMethod;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.exception.BeanDefinitionNotFoundException;
import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ReflectionUtil;

public abstract class AbstractBeanDefinition implements BeanDefinition{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractBeanDefinition.class);
	
	protected final BeanContainer beanContainer;
	protected final Class<?> beanType;
	protected final String beanName;
	protected final BeanScope beanScope;
	protected final Constructor<?> constructor;
	
	protected List<Method> initialMethods;
	protected List<Method> destoryMethods;
	
	public AbstractBeanDefinition(BeanContainer beanContainer,Class<?> beanType,String beanName,BeanScope beanScope){
		this.beanContainer = beanContainer;
		this.beanType = beanType;
		this.beanName = beanName;
		this.beanScope = beanScope;
		this.constructor = this.choseConstructor(this.beanType);
	}
	
	@Override
	public Class<?> getBeanType(){
		return this.beanType;
	}

	@Override
	public String getBeanName(){
		return this.beanName;
	}

	@Override
	public boolean isPrototype(){
		return this.beanScope == BeanScope.Prototype;
	}

	@Override
	public boolean isSingleten(){
		return this.beanScope == BeanScope.Singleton;
	}
	
	@Override
	public boolean matchType(Class<?> requireType){
		if(null == requireType){
			return false;
		}
		if(requireType.isAssignableFrom(this.beanType)){
			return true;
		}
		return false;
	}

	@Override
	public Constructor<?> getConstructor(){
		return this.constructor;
	}

	@Override
	public BeanContainer getBeanContainer(){
		return this.beanContainer;
	}
	
	@Override
	public void initial(){
		this.initialMethods = this.searchMethods(true);
		this.destoryMethods = this.searchMethods(false);
		log.debug("initial {} success",this.toString());
	}
	
	@Override
	public void destory(){
		this.initialMethods = null;
		this.destoryMethods = null;
		log.debug("destory {} success",this.toString());
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj){
		if(null == obj){
			return false;
		}
		return this.toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return String.format("%s#%s",this.beanType.getName(),this.beanName);
	}
	
	protected boolean needCreateProxy(){
		return this.beanContainer.getBeanContainerHandlerChain().onNeedProxy(beanContainer,this);
	}
	
	//调用初始化方法
	protected void invokeInitialMethod(Object object){
		if(null == this.initialMethods || this.initialMethods.isEmpty()){
			return;
		}
		for(Method method : this.initialMethods){
			this.invokeMethodAndAutowired(method,object);
		}
	}
	
	//调用销毁方法
	protected void invokeDestoryMethod(Object object){
		if(null == this.destoryMethods || this.destoryMethods.isEmpty()){
			return;
		}
		for(Method method : this.destoryMethods){
			this.invokeMethodAndAutowired(method,object);
		}
	}
	
	//注入
	protected void injectObject(Object object){
		this.beanContainer.getBeanContainerHandlerChain().onInject(this.beanContainer,this,object);
	}
	
	protected Constructor<?> choseConstructor(Class<?> type){
		return choseClassConstructor(type);
	}
	
	//调用方法并自动注入参数
	protected Object invokeMethodAndAutowired(Method method,Object object){
		Class<?>[] parameterTypes = method.getParameterTypes();
		if(null == parameterTypes || parameterTypes.length == 0){
			return ReflectionUtil.invokeMethod(method,object);
		}else{
			Annotation[] annotations = AnnotationUtil.getAnnotationFromParameter(method,Autowired.class);
			Object[] parameterDatas = new Object[parameterTypes.length];
			for(int i=0;i<parameterTypes.length;i++){
				Class<?> parameterType = parameterTypes[i];
				if(null == annotations[i]){
					parameterDatas[i] = null;
				}else{
					Autowired autowired = (Autowired)annotations[i];
					parameterDatas[i] = this.getBeanInstance(parameterType,autowired.value());
				}
			}
			return ReflectionUtil.invokeMethod(method,object,parameterDatas);
		}
	}
	
	protected Object invokeConstructorAndAutowired(Constructor<?> constructor){
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		if(null == parameterTypes || parameterTypes.length == 0){
			return ReflectionUtil.invokeConstructor(constructor);
		}else{
			Annotation[] annotations = AnnotationUtil.getAnnotationFromParameter(constructor,Autowired.class);
			Object[] parameterDatas = new Object[parameterTypes.length];
			for(int i=0;i<parameterTypes.length;i++){
				Class<?> parameterType = parameterTypes[i];
				if(null == annotations[i]){
					parameterDatas[i] = null;
				}else{
					Autowired autowired = (Autowired)annotations[i];
					parameterDatas[i] = this.getBeanInstance(parameterType,autowired.value());
				}
			}
			return ReflectionUtil.invokeConstructor(constructor,parameterDatas);
		}
	}

	protected List<Method> searchMethods(final boolean isInitialMethodAnnotation){
		List<Method> methods = new ArrayList<Method>();
		for(Method method : ReflectionUtil.getDeclaredMethods(this.beanType)){
			if(AnnotationUtil.hasAnnotation(method,isInitialMethodAnnotation ? InitialMethod.class : DestoryMethod.class)){
				ReflectionUtil.makeAccessible(method);
				methods.add(method);
			}
		}
		Collections.sort(methods,new Comparator<Method>(){
			@Override
			public int compare(Method o1,Method o2){
				if(isInitialMethodAnnotation){
					InitialMethod initialMethodA = AnnotationUtil.getAnnotation(o1,InitialMethod.class);
					InitialMethod initialMethodB = AnnotationUtil.getAnnotation(o2,InitialMethod.class);
					return new Integer(initialMethodA.sort()).compareTo(initialMethodB.sort());
				}else{
					DestoryMethod initialMethodA = AnnotationUtil.getAnnotation(o1,DestoryMethod.class);
					DestoryMethod initialMethodB = AnnotationUtil.getAnnotation(o2,DestoryMethod.class);
					return new Integer(initialMethodA.sort()).compareTo(initialMethodB.sort());
				}
			}
		});
		return Collections.unmodifiableList(methods);
	}
	
	protected Object getBeanInstance(Class<?> requireType,String beanName){
		Object instance = this.beanContainer.getBeanDefinition(requireType,beanName);
		if(null == instance){
			throw new BeanDefinitionNotFoundException(requireType.getName());
		}
		return instance;
	}
	
	protected Object createInstance(BeanContainer beanContainer,BeanDefinition beanDefinition){
		if(this.needCreateProxy()){
			return this.beanContainer.getProxyFactory().createProxy(beanContainer,beanDefinition);
		}else{
			return this.invokeConstructorAndAutowired(this.constructor);
		}
	}
	
	// 选择最优的构造函数
	// 1.默认搜索参数含有@Autowired注解的构造函数,如果多个构造函数都存在@Autowired注解,则使用最先搜索到的
	// 2.如果搜索不到@Autowired注解的构造函数,则使用无参构造函数
	// 3.如果没有无参构造函数,则使用当前搜索的构造函数,参数传递值为null
	protected static Constructor<?> choseClassConstructor(Class<?> type){
		Constructor<?>[] constructors = type.getConstructors();
		Constructor<?> noAgreementsConstructor = null;
		for(Constructor<?> constructor : constructors){
			Class<?>[] agreementTypes = constructor.getParameterTypes();
			if(null == agreementTypes || agreementTypes.length == 0){//选择无参构造函数
				noAgreementsConstructor = constructor;
			}
			if(AnnotationUtil.hasAnnotationFromParameter(constructor,Autowired.class)){
				return constructor;
			}
		}
		if(null != noAgreementsConstructor){
			return noAgreementsConstructor;
		}
		return constructors[0];
	}
	
}
