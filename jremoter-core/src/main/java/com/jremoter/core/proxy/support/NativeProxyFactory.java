package com.jremoter.core.proxy.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("native")
public class NativeProxyFactory extends AbstractProxyFactory{

	@Override
	protected Object doCreateProxy(BeanContainer beanContainer,BeanDefinition beanDefinition) {
		return null;
	}

}