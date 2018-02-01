/**   
 * @title: AutoWired.java 
 * @package io.nix.core.annotation 
 * @description: Bean的自动注入注解
 * @author TangBin tb_bin@163.com
 * @date 2018年1月15日 下午6:45:04 
 * @version 1.0.0
 * 注意:当前注解可以标注的field,method,parameter上,标注的method上时必须有参数,标注在parameter上时其他未标注的参数值为null
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
	
	//Bean实例化名称,默认可为空,当为空时则使用类名的骆驼命名法
	public String value() default "";
	
}