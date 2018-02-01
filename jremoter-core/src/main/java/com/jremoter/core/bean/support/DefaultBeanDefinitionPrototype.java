package com.jremoter.core.bean.support;

import java.util.LinkedHashSet;
import java.util.Set;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.util.GenericTypeUtil;

public class DefaultBeanDefinitionPrototype extends AbstractBeanDefinition{
	
	protected Set<Object> objects;
	
	public DefaultBeanDefinitionPrototype(BeanContainer beanContainer,Class<?> beanType, String beanName) {
		super(beanContainer, beanType, beanName, BeanScope.Prototype);
		this.objects = new LinkedHashSet<Object>();
	}
	
	@Override
	public <T> T getBeanInstance(){
		Object instance = this.createInstance(this.beanContainer,this);
		this.injectObject(instance);
		this.invokeInitialMethod(instance);
		this.objects.add(instance);
		return GenericTypeUtil.parseType(instance);
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
		for(Object object : this.objects){
			this.invokeDestoryMethod(object);
		}
	}

	@Override
	public void destory(){
		this.objects.clear();
		super.destory();
	}
	
}