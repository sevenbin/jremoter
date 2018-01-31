package com.jremoter.core.bean;

import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.toolkit.Extension;

@Extension
public interface BeanContainerFactory {
	
	public BeanContainer createBeanContainer(ApplicationContext applicationContext);
	
}