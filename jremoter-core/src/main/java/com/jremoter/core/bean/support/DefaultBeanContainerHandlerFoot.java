package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainerHandler;
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
	
}