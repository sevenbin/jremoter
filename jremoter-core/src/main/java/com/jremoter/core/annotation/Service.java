/**   
 * @title: Service.java 
 * @package io.nix.core.annotation 
 * @description: 服务标注注解
 * @author TangBin tb_bin@163.com
 * @date 2018年1月15日 下午6:40:42 
 * @version 1.0.0
 * 注意:当前注解必须标注在class或method上才会生效,标注在method上必须有参数,否则抛出异常
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jremoter.core.bean.BeanScope;

@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
	
	//Bean实例化名称,默认可为空,当为空时则使用类名的骆驼命名法
	public String value() default "";
	
	//Bean实例化方式,默认为单例
	public BeanScope scope() default BeanScope.Singleton;
	
}