package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainerHandler;
import com.jremoter.core.bean.BeanContainerHandlerContext;
import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.handler.support.AbstractHandlerContext;

public class DefaultBeanContainerHandlerContext extends AbstractHandlerContext<BeanContainerHandler> implements BeanContainerHandlerContext{

	public DefaultBeanContainerHandlerContext(HandlerChain<BeanContainerHandler> chain, String name,BeanContainerHandler handler) {
		super(chain, name, handler);
	}
	
	
	
}