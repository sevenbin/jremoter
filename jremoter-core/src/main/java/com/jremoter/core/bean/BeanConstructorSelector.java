package com.jremoter.core.bean;

import java.lang.reflect.Constructor;

import com.jremoter.core.toolkit.Extension;

@Extension
public interface BeanConstructorSelector {
	
	public Constructor<?> select(Class<?> requireType);
	
}