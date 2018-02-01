package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerHandler;
import com.jremoter.core.bean.BeanContainerHandlerChain;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.handler.HandlerContext;
import com.jremoter.core.handler.support.AbstractHandlerChain;

public class DefaultBeanContainerHandlerChain extends AbstractHandlerChain<BeanContainerHandler> implements BeanContainerHandlerChain{
	
	private BeanContainer beanContainer;
	
	public DefaultBeanContainerHandlerChain(BeanContainer beanContainer){
		this.beanContainer = beanContainer;
	}
	
	@Override
	public BeanContainer getBeanContainer() {
		return this.beanContainer;
	}
	
	@Override
	protected HandlerContext<BeanContainerHandler> createHeadHandlerContext(){
		return new DefaultBeanContainerHandlerHead(this);
	}

	@Override
	protected HandlerContext<BeanContainerHandler> createFootHandlerContext(){
		return new DefaultBeanContainerHandlerFoot(this);
	}
	
	@Override
	protected HandlerContext<BeanContainerHandler> createHandlerContext(String name, BeanContainerHandler handler){
		return new DefaultBeanContainerHandlerContext(this,name,handler);
	}

	@Override
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance){
		HandlerContext<BeanContainerHandler> context = this.head.getNext();
		while(context != this.foot){
			context.getHandler().onInject(beanContainer, beanDefinition, instance);
			context = context.getNext();
		}
	}

	@Override
	public boolean onNeedProxy(BeanContainer beanContainer,BeanDefinition beanDefinition){
		boolean result = false;
		HandlerContext<BeanContainerHandler> context = this.head.getNext();
		while(context != this.foot){
			result = context.getHandler().onNeedProxy(beanContainer,beanDefinition);
			context = context.getNext();
		}
		return result;
	}
	
}