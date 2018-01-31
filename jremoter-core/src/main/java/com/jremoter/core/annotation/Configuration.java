/**   
 * @title: Configuration.java 
 * @package com.jremoter.core.annotation 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月24日 上午11:21:10 
 * @version 1.0.0
 * @description: 配置项
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {
	
	//可配置的扫描包,支持通配符
	//例子:
	//		io.nix.*	-> 扫描io.nix包下的class,不包含子包
	//		io.nix.**	-> 扫描io.nix包下的所有class,包含子包
	public String[] patterns() default {};
	
}