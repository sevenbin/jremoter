package com.jremoter.core.bean.support;

import java.lang.reflect.Constructor;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.exception.BeanConstructorSelectorException;
import com.jremoter.core.toolkit.ExtensionName;
import com.jremoter.core.util.AnnotationUtil;

/**
 * 默认的构造函数选择策略
 * @author koko
 *
 */
@ExtensionName("default")
public class DefaultBeanConstructorSelector extends AbstractBeanConstructorSelector{
	
	// 选择最优的构造函数
	// 1.默认搜索参数含有@Autowired注解的构造函数,如果多个构造函数都存在@Autowired注解,则使用最先搜索到的
	// 2.如果搜索不到@Autowired注解的构造函数,则使用无参构造函数
	// 3.如果没有无参构造函数,则使用当前搜索的构造函数,参数传递值为null
	
	@Override
	protected Constructor<?> doSelect(Class<?> requireType)throws BeanConstructorSelectorException {
		Constructor<?>[] constructors = requireType.getConstructors();
		Constructor<?> noAgreementsConstructor = null;
		for(Constructor<?> constructor : constructors){
			Class<?>[] agreementTypes = constructor.getParameterTypes();
			if(null == agreementTypes || agreementTypes.length == 0){//选择无参构造函数
				noAgreementsConstructor = constructor;
			}
			if(AnnotationUtil.hasAnnotation(constructor,Autowired.class)){
				return constructor;
			}
		}
		if(null != noAgreementsConstructor){
			return noAgreementsConstructor;
		}
		return constructors[0];
	}
	
}