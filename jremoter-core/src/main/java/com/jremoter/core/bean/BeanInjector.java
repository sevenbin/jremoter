package com.jremoter.core.bean;


public interface BeanInjector {
	
	public Object inject(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance);
	
}