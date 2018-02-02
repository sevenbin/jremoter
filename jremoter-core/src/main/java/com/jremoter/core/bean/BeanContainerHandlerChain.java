package com.jremoter.core.bean;

import com.jremoter.core.handler.HandlerChain;

public interface BeanContainerHandlerChain extends HandlerChain<BeanContainerHandler>{
	
	public BeanContainer getBeanContainer();
	
	public void onCreate(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance);
	
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance);
	
	public boolean onNeedProxy(BeanContainer beanContainer,BeanDefinition beanDefinition);
	
}