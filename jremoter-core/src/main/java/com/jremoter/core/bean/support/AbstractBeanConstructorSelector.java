package com.jremoter.core.bean.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import com.jremoter.core.bean.BeanConstructorSelector;
import com.jremoter.core.exception.BeanConstructorSelectorException;

public abstract class AbstractBeanConstructorSelector implements BeanConstructorSelector{

	@Override
	public Constructor<?> select(Class<?> requireType)throws BeanConstructorSelectorException{
		if(null == requireType){
			throw new NullPointerException("requireType");
		}
		if(requireType.isInterface()){
			throw new BeanConstructorSelectorException("require type is interface " + requireType.getName());
		}
		if(Modifier.isAbstract(requireType.getModifiers())){
			throw new BeanConstructorSelectorException("require type is absolute " + requireType.getName());
		}
		return this.doSelect(requireType);
	}
	
	protected abstract Constructor<?> doSelect(Class<?> requireType)throws BeanConstructorSelectorException;
	
}