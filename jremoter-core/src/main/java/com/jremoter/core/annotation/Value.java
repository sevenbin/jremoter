/**   
 * @title: Value.java 
 * @package com.jremoter.core.annotation 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月29日 下午5:07:37 
 * @version 1.0.0
 * @description: 属性注入
 */
package com.jremoter.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
	
	public String value();
	
}