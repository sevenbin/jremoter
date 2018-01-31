package com.jremoter.core.proxy.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.proxy.ProxyFactory;

public abstract class AbstractProxyFactory implements ProxyFactory{

	@Override
	public Object createProxy(BeanContainer beanContainer,BeanDefinition beanDefinition){
		return this.doCreateProxy(beanContainer,beanDefinition);
	}
	
	protected abstract Object doCreateProxy(BeanContainer beanContainer,BeanDefinition beanDefinition);
	
}