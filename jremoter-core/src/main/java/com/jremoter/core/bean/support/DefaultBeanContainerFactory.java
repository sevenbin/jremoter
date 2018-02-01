package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerFactory;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("default")
public class DefaultBeanContainerFactory implements BeanContainerFactory{

	@Override
	public BeanContainer createBeanContainer(ApplicationContext applicationContext){
		return new DefaultBeanContainer(applicationContext);
	}
	
}