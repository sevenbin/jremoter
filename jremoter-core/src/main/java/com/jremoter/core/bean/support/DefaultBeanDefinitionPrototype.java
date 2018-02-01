package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanScope;

public class DefaultBeanDefinitionPrototype extends AbstractBeanDefinition{

	public DefaultBeanDefinitionPrototype(BeanContainer beanContainer,Class<?> beanType, String beanName) {
		super(beanContainer, beanType, beanName, BeanScope.Prototype);
	}
	
	@Override
	public <T> T getBeanInstance(){
		return null;
	}
	
	@Override
	public void create(){
		
	}

	@Override
	public void afterCreate(){
		
	}

	@Override
	public void inject(){
		
	}

	@Override
	public void afterInject(){
		
	}

	@Override
	public void beforeDestory(){
		
	}
	
}