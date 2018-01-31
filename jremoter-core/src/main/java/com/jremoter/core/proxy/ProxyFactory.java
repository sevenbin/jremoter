package com.jremoter.core.proxy;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.toolkit.Extension;

@Extension
public interface ProxyFactory {
	
	public Object createProxy(BeanContainer beanContainer,BeanDefinition beanDefinition);
	
}