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
import com.jremoter.core.bean.BeanCreator;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanInjector;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.exception.BeanDefinitionNotFoundException;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.GenericTypeUtil;
import com.jremoter.core.util.ReflectionUtil;

public abstract class AbstractBeanDefinition implements BeanDefinition{
	
	private BeanContainer beanContainer;
	private BeanCreator beanCreator;
	private BeanInjector beanInjector;
	private Class<?> beanType;
	private String beanName;
	private BeanScope beanScope;
	private Constructor<?> constructor;
	
	private List<Method> initialMethods;
	private List<Method> destoryMethods;
	
	public AbstractBeanDefinition(BeanContainer beanContainer,BeanCreator beanCreator,BeanInjector beanInjector,Class<?> beanType,String beanName,BeanScope beanScope){
		this.beanContainer = beanContainer;
		this.beanCreator = beanCreator;
		this.beanInjector = beanInjector;
		this.beanType = beanType;
		this.beanName = beanName;
		this.beanScope = beanScope;
		this.constructor = this.selectConstructor();
		this.initialMethods = new ArrayList<Method>();
		this.destoryMethods = new ArrayList<Method>();
		this.initialMethods.addAll(this.initMethods(InitialMethod.class));
		this.destoryMethods.addAll(this.initMethods(DestoryMethod.class));
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
	public <T> T getBeanInstance(){
		return GenericTypeUtil.parseType(this.doGetBeanInstance());
	}
	
	@Override
	public void initial(){
		this.doInitial();
	}

	@Override
	public void create(){
		this.doCreate();
	}
	
	@Override
	public void afterCreate() {
		this.doAfterCreate();
	}

	@Override
	public void inject(){
		this.doInitial();
	}
	
	@Override
	public void afterInject() {
		this.doAfterInject();
	}
	
	@Override
	public void beforeDestory() {
		this.doBeforeDestory();
	}

	@Override
	public void destory(){
		this.doDestory();
	}
	
	protected Constructor<?> selectConstructor(){
		return this.beanContainer.getBeanConstructorSelector().select(this.beanType);
	}
	
	protected Object createInstance(){
		return this.beanCreator.create(this.beanContainer,this);
	}
	
	protected Object injectInstance(Object instance){
		return this.beanInjector.inject(this.beanContainer,this,instance);
	}
	
	protected List<Method> initMethods(final Class<? extends Annotation> annotationClass){
		List<Method> methods = new ArrayList<Method>();
		for(Method method : ReflectionUtil.getDeclaredMethods(this.beanType)){
			if(AnnotationUtil.hasAnnotation(method,annotationClass)){
				ReflectionUtil.makeAccessible(method);
				methods.add(method);
			}
		}
		Collections.sort(methods,new Comparator<Method>(){
			@Override
			public int compare(Method o1,Method o2){
				if(annotationClass.equals(InitialMethod.class)){
					InitialMethod initialMethodA = AnnotationUtil.getAnnotation(o1,InitialMethod.class);
					InitialMethod initialMethodB = AnnotationUtil.getAnnotation(o2,InitialMethod.class);
					return new Integer(initialMethodA.sort()).compareTo(initialMethodB.sort());
				}else{
					DestoryMethod destoryMethodA = AnnotationUtil.getAnnotation(o1,DestoryMethod.class);
					DestoryMethod destoryMethodB = AnnotationUtil.getAnnotation(o2,DestoryMethod.class);
					return new Integer(destoryMethodA.sort()).compareTo(destoryMethodB.sort());
				}
			}
		});
		return methods;
	}
	
	protected Object getBeanInstance(Class<?> requireType,String beanName){
		if(null == requireType){
			return null;
		}
		if(ApplicationContext.class.isAssignableFrom(requireType)){
			return this.beanContainer.getApplicationContext();
		}else if(BeanContainer.class.isAssignableFrom(requireType)){
			return this.beanContainer;
		}else{
			BeanDefinition beanDefinition = this.beanContainer.getAutowiredBeanDefinition(requireType,beanName);
			if(null == beanDefinition){
				throw new BeanDefinitionNotFoundException();
			}
			return beanDefinition.getBeanInstance();
		}
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
	
	protected abstract Object doGetBeanInstance();
	
	protected void doInitial(){
		
	}
	
	protected void doCreate(){
		
	}
	
	protected void doAfterCreate(){
		
	}
	
	protected void doInject(){
		
	}
	
	protected void doAfterInject(){
		
	}
	
	protected void doBeforeDestory(){
		
	}
	
	protected void doDestory(){
		
	}
	
}