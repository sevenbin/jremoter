package com.jremoter.core.bean;

import java.util.Map;

import com.jremoter.core.context.ApplicationContext;

public interface BeanContainer {
	
	public ApplicationContext getApplicationContext();
	public BeanConstructorSelector getBeanConstructorSelector();
	
	//初始化容器
	public void initial();
	
	//销毁容器
	public void destory();
	
	//注册Bean
	public BeanDefinition attachBean(Class<?> requireType,BeanScope requireScope,String beanName);
	//注册实例对象Bean
	public BeanDefinition attachBean(Class<?> requireType,String beanName,Object beanInstance);
	
	public Map<String,BeanDefinition> getBeanDefinitions();
	public Map<String,BeanDefinition> getBeanDefinitions(Class<?> requireType);
	
	public BeanDefinition getBeanDefinition(Class<?> requireType,String beanName);
	//获取可注入的BeanDefinition对象,会抛出异常
	public BeanDefinition getAutowiredBeanDefinition(Class<?> requireType,String beanName);
	
}