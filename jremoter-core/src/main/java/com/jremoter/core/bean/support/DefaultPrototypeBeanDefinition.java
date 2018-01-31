package com.jremoter.core.bean.support;

import java.util.LinkedHashSet;
import java.util.Set;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanCreator;
import com.jremoter.core.bean.BeanInjector;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.util.GenericTypeUtil;

public class DefaultPrototypeBeanDefinition extends AbstractBeanDefinition{
	
	protected Set<Object> objects;
	
	public DefaultPrototypeBeanDefinition(BeanContainer beanContainer,BeanCreator beanCreator, BeanInjector beanInjector,Class<?> beanType, String beanName) {
		super(beanContainer, beanCreator, beanInjector, beanType, beanName,BeanScope.Prototype);
		this.objects = new LinkedHashSet<Object>();
	}
	
	@Override
	protected Object doGetBeanInstance(){
		Object instance = this.createInstance();
		this.injectInstance(instance);
		this.invokeInitialMethod(instance);
		this.objects.add(instance);
		return GenericTypeUtil.parseType(instance);
	}
	
}