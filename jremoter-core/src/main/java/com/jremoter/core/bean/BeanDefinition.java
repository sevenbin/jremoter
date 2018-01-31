package com.jremoter.core.bean;

import java.lang.reflect.Constructor;

public interface BeanDefinition {
	
	public Class<?> getBeanType();
	public String getBeanName();
	
	public boolean isPrototype();
	public boolean isSingleten();
	
	public <T> T getBeanInstance();
	
	public boolean matchType(Class<?> requireType);
	
	public Constructor<?> getConstructor();
	
	public BeanContainer getBeanContainer();
	
	public void initial();
	public void create();
	public void afterCreate();
	public void inject();
	public void afterInject();
	public void beforeDestory();
	public void destory();
	
}