/**   
 * @title: DestoryMethod.java 
 * @package io.nix.core.annotation 
 * @description: Bean销毁处理
 * @author TangBin tb_bin@163.com
 * @date 2018年1月15日 下午6:48:29 
 * @version 1.0.0
 * 注意:此注解为销毁bean之前调用,执行用户自定义销毁内容
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DestoryMethod {
	
	//排序标记,值越小越靠前,即越先执行,如果默认都是0,则按照程序搜索顺序执行
	public int sort() default 100;
	
}