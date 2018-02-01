package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerHandler;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.handler.HandlerChain;

public class DefaultBeanContainerHandlerFoot extends DefaultBeanContainerHandlerContext implements BeanContainerHandler{
	
	private static final String NAME = "_FOOT";
	
	public DefaultBeanContainerHandlerFoot(HandlerChain<BeanContainerHandler> chain) {
		super(chain, NAME, null);
	}

	@Override
	public BeanContainerHandler getHandler(){
		return this;
	}

	@Override
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance){
		
	}
	
}