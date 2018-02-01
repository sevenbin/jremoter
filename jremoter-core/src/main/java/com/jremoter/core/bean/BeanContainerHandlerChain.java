package com.jremoter.core.bean;

import com.jremoter.core.handler.HandlerChain;

public interface BeanContainerHandlerChain extends HandlerChain<BeanContainerHandler>{
	
	public BeanContainer getBeanContainer();
	
}