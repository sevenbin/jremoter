package com.jremoter.core.bean.inject;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.support.DefaultBeanContainerHandler;
import com.jremoter.core.exception.BeanDefinitionNotFoundException;
import com.jremoter.core.option.Configuration;
import com.jremoter.core.option.Option;
import com.jremoter.core.option.support.AbstractConfiguration;
import com.jremoter.core.util.ObjectUtil;
import com.jremoter.core.util.StringUtil;

public abstract class AbstractBeanInject extends DefaultBeanContainerHandler{
	
	protected Configuration configuration;
	
	public AbstractBeanInject(){
		this.configuration = AbstractConfiguration.getConfiguration();
	}
	
	protected Object getConfigurationValue(String optionName,Class<?> type){
		Option<String> tempOption = Option.create(optionName,null);
		String value = this.configuration.getOption(tempOption);
		if(StringUtil.isBlank(value)){
			return null;
		}
		return ObjectUtil.parseType(type,value);
	}
	
	protected Object getBeanInstance(BeanContainer beanContainer,Class<?> requireType,String beanName){
		BeanDefinition injectBeanDefinition = beanContainer.getBeanDefinition(requireType,beanName);
		if(null == injectBeanDefinition){
			throw new BeanDefinitionNotFoundException();
		}
		Object result = injectBeanDefinition.getBeanInstance();
		if(null == result){
			throw new BeanDefinitionNotFoundException();
		}
		return result;
	}
	
	@Override
	public void onInject(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance) {
		this.doInject(beanContainer, beanDefinition, instance);
	}

	protected abstract void doInject(BeanContainer beanContainer,BeanDefinition beanDefinition,Object instance);
	
}