package com.jremoter.core.bean.support;

import java.lang.reflect.Constructor;

import com.jremoter.core.bean.BeanContainer;

public class DefaultBeanDefinitionSingletonInstance extends DefaultBeanDefinitionSingleton{

	public DefaultBeanDefinitionSingletonInstance(BeanContainer beanContainer,Class<?> beanType, String beanName,Object instance) {
		super(beanContainer, beanType, beanName);
		this.instance = instance;
	}
	
	@Override
	protected Constructor<?> choseConstructor(Class<?> type) {
		return null;
	}
	
}