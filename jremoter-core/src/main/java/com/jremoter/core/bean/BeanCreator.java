package com.jremoter.core.bean;

public interface BeanCreator {
	
	public Object create(BeanContainer beanContainer,BeanDefinition beanDefinition);
	
}