package com.jremoter.core.bean;

import com.jremoter.core.handler.Handler;

public interface BeanContainerHandler extends Handler{
	
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance);
	
}