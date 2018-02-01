package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.util.GenericTypeUtil;

public class DefaultBeanDefinitionSingleton extends AbstractBeanDefinition{
	
	protected Object instance;
	
	public DefaultBeanDefinitionSingleton(BeanContainer beanContainer,Class<?> beanType,String beanName) {
		super(beanContainer, beanType, beanName, BeanScope.Singleton);
	}

	@Override
	public <T> T getBeanInstance(){
		return GenericTypeUtil.parseType(this.instance);
	}

	@Override
	public void create(){
		this.instance = this.createInstance(this.beanContainer,this);
		this.afterCreate();
	}

	@Override
	public void afterCreate(){
		
	}

	@Override
	public void inject(){
		this.injectObject(this.instance);
		this.afterInject();
	}
	
	@Override
	public void afterInject(){
		this.invokeInitialMethod(this.instance);
	}

	@Override
	public void beforeDestory(){
		this.invokeDestoryMethod(this.instance);
	}

	@Override
	public void destory(){
		this.instance = null;
		super.destory();
	}
	
}