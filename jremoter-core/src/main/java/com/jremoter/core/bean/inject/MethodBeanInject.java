package com.jremoter.core.bean.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.Value;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.exception.BeanDefinitionAutowireException;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ClassUtil;
import com.jremoter.core.util.ReflectionUtil;
import com.jremoter.core.util.StringUtil;

public class MethodBeanInject extends AbstractBeanInject{

	@Override
	protected void doInject(BeanContainer beanContainer,BeanDefinition beanDefinition, Object instance) {
		
		Class<?> type = beanDefinition.getBeanType();
		for(Method method : ReflectionUtil.getDeclaredMethods(type)){
			if(!AnnotationUtil.hasAnnotation(method,Autowired.class)){
				continue;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(null == parameterTypes || parameterTypes.length == 0){
				throw new BeanDefinitionAutowireException(String.format("the method requires at least one parameter : %s#%s",type.getName(),method.getName()));
			}
			Annotation[] autowiredAnnotations = AnnotationUtil.getAnnotationFromParameter(method,Autowired.class);
			Annotation[] valueAnnotations = AnnotationUtil.getAnnotationFromParameter(method,Value.class);
			Object[] parameterDatas = new Object[parameterTypes.length];
			for(int i=0;i<parameterTypes.length;i++){
				Class<?> parameterType = parameterTypes[i];
				String beanName = ClassUtil.getCamelClassName(parameterType);
				if(null != autowiredAnnotations[i]){
					Autowired autowired = (Autowired)autowiredAnnotations[i];
					if(StringUtil.isNotBlank(autowired.value())){
						beanName = autowired.value();
					}
					parameterDatas[i] = this.getBeanInstance(beanContainer,parameterType,beanName);
				}else{
					if(null != valueAnnotations[i]){
						Value value = (Value)valueAnnotations[i];
						String valueName = value.value();
						if(StringUtil.isNotBlank(valueName)){
							parameterDatas[i] = this.getConfigurationValue(valueName,parameterType);
						}
					}
				}
			}
			ReflectionUtil.makeAccessible(method);
			ReflectionUtil.invokeMethod(method,instance,parameterDatas);
		}
		
	}
	
}