package com.jremoter.core.bean.inject;

import java.lang.reflect.Field;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.Value;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.exception.BeanDefinitionAutowireException;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ReflectionUtil;
import com.jremoter.core.util.StringUtil;

public class FieldBeanInject extends AbstractBeanInject{

	@Override
	protected void doInject(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance) {
		Class<?> type = beanDefinition.getBeanType();
		for(Field field : ReflectionUtil.getDeclaredFields(type)){
			if(AnnotationUtil.hasAnnotation(field,Autowired.class)){
				Autowired autowired = AnnotationUtil.getAnnotation(field,Autowired.class);
				String beanName = autowired.value();
				Class<?> beanType = field.getType();
				try{
					ReflectionUtil.makeAccessible(field);
					field.set(instance,this.getBeanInstance(beanContainer,beanType,beanName));
				}catch(Throwable t){
					t.printStackTrace();
					throw new BeanDefinitionAutowireException(t);
				}
			}
			if(AnnotationUtil.hasAnnotation(field,Value.class)){
				Value value = AnnotationUtil.getAnnotation(field,Value.class);
				String valueName = value.value();
				if(StringUtil.isNotBlank(valueName)){
					try{
						ReflectionUtil.makeAccessible(field);
						field.set(instance,this.getConfigurationValue(valueName,field.getType()));
					}catch(Throwable t){
						t.printStackTrace();
						throw new BeanDefinitionAutowireException(t);
					}
				}
			}
		}
	}
	
}