package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerHandler;
import com.jremoter.core.bean.BeanDefinition;

public class DefaultBeanContainerHandler implements BeanContainerHandler{
	
	@Override
	public void onCreate(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance) {
		
	}

	@Override
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance){
		
	}

	@Override
	public boolean onNeedProxy(BeanContainer beanContainer,BeanDefinition beanDefinition) {
		return false;
	}
	
}