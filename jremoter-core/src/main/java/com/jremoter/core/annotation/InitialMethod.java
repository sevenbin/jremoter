/**   
 * @title: InitialMethod.java 
 * @package io.nix.core.annotation 
 * @description: Bean后处理注解
 * @author TangBin tb_bin@163.com
 * @date 2018年1月15日 下午6:47:18 
 * @version 1.0.0
 * 注意:此注解标注的方法会在bean实例化成功,且注入结束后调用
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
public @interface InitialMethod {
	
	//排序标记,值越小越靠前,即越先执行,如果默认都是0,则按照程序搜索顺序执行
	public int sort() default 100;
	
}