package com.jremoter.core.bean.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.util.GenericTypeUtil;

public class DefaultBeanDefinitionPrototypeFromMethod extends DefaultBeanDefinitionPrototype{
	
	private BeanDefinition methodBeanDefinition;
	private Method method;
	
	public DefaultBeanDefinitionPrototypeFromMethod(BeanContainer beanContainer, Class<?> beanType, String beanName,BeanDefinition methodBeanDefinition,Method method) {
		super(beanContainer, beanType, beanName);
	}
	
	@Override
	public <T> T getBeanInstance() {
		Object instance = this.invokeMethodAndAutowired(this.method,this.methodBeanDefinition.getBeanInstance());
		this.injectObject(instance);
		this.invokeInitialMethod(instance);
		this.objects.add(instance);
		return GenericTypeUtil.parseType(instance);
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