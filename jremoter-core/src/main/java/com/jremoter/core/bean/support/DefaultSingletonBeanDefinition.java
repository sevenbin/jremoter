package com.jremoter.core.bean.support;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanCreator;
import com.jremoter.core.bean.BeanInjector;
import com.jremoter.core.bean.BeanScope;

public class DefaultSingletonBeanDefinition extends AbstractBeanDefinition{
	
	private Object instance;
	
	public DefaultSingletonBeanDefinition(BeanContainer beanContainer,BeanCreator beanCreator, BeanInjector beanInjector,Class<?> beanType, String beanName) {
		super(beanContainer, beanCreator, beanInjector, beanType, beanName,BeanScope.Singleton);
	}
	
	@Override
	protected Object doGetBeanInstance() {
		return this.instance;
	}

	@Override
	protected void doInitial(){
		
	}

	@Override
	protected void doCreate(){
		this.instance = this.createInstance();
	}

	@Override
	protected void doAfterCreate(){
		
	}

	@Override
	protected void doInject(){
		this.injectInstance(this.instance);
	}
	
	@Override
	protected void doAfterInject(){
		
	}

	@Override
	protected void doBeforeDestory(){
		
	}

	@Override
	protected void doDestory(){
		
	}
	
}