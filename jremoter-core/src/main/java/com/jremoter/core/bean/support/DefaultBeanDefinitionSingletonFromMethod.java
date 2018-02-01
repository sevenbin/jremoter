package com.jremoter.core.bean.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;

public class DefaultBeanDefinitionSingletonFromMethod extends DefaultBeanDefinitionSingleton{

	private BeanDefinition methodBeanDefinition;
	private Method method;
	
	public DefaultBeanDefinitionSingletonFromMethod(BeanContainer beanContainer, Class<?> beanType, String beanName,BeanDefinition methodBeanDefinition,Method method) {
		super(beanContainer, beanType, beanName);
		this.methodBeanDefinition = methodBeanDefinition;
		this.method = method;
	}

	@Override
	public void create(){
		this.instance = this.invokeMethodAndAutowired(this.method,this.methodBeanDefinition.getBeanInstance());
	}

	@Override
	protected Constructor<?> choseConstructor(Class<?> type) {
		return null;
	}
	
	public Method getMethod(){
		return this.method;
	}
	
	public BeanDefinition getMethodBeanDefinition(){
		return this.methodBeanDefinition;
	}
	
}