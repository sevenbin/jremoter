package com.jremoter.core.bean.support;

import java.lang.reflect.Method;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanDefinitionFactory;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("default")
public class DefaultBeanDefinitionFactory implements BeanDefinitionFactory{

	@Override
	public BeanDefinition createBeanDefinition(BeanContainer beanContainer,Class<?> requireType, BeanScope requireScope, String beanName){
		if(null == requireScope || requireScope == BeanScope.Singleton){
			return new DefaultBeanDefinitionSingleton(beanContainer, requireType, beanName);
		}else{
			return new DefaultBeanDefinitionPrototype(beanContainer, requireType, beanName);
		}
	}

	@Override
	public BeanDefinition createBeanDefinition(BeanContainer beanContainer,Class<?> requireType, String beanName, Object beanInstance) {
		return new DefaultBeanDefinitionSingletonInstance(beanContainer, requireType, beanName, beanInstance);
	}

	@Override
	public BeanDefinition createBeanDefinition(BeanContainer beanContainer,Class<?> requireType, BeanScope requireScope, String beanName,BeanDefinition methodBeanDefinition, Method method) {
		if(null == requireScope || requireScope == BeanScope.Singleton){
			return new DefaultBeanDefinitionSingletonFromMethod(beanContainer, requireType, beanName, methodBeanDefinition, method);
		}else{
			return new DefaultBeanDefinitionPrototypeFromMethod(beanContainer, requireType, beanName, methodBeanDefinition, method);
		}
	}
	
}