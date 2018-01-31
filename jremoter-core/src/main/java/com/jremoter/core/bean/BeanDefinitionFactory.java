package com.jremoter.core.bean;

import com.jremoter.core.toolkit.Extension;

@Extension
public interface BeanDefinitionFactory {
	
	public BeanDefinition createBeanDefinition(BeanContainer beanContainer,Class<?> requireType,BeanScope requireScope,String beanName);
	public BeanDefinition createBeanDefinition(BeanContainer beanContainer,Class<?> requireType,String beanName,Object beanInstance);
	
}