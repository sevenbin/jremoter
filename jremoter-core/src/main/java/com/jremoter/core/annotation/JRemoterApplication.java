/**   
 * @title: JRemoterApplication.java 
 * @package com.jremoter.core.annotation 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月24日 上午11:19:42 
 * @version 1.0.0
 * @description: runner class 入口注解
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jremoter.core.Constant;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JRemoterApplication {
	
	public String name() default Constant.V_CONFIGURATION_NAME;
	public String path() default Constant.V_CONFIGURATION_PATH;
	
}