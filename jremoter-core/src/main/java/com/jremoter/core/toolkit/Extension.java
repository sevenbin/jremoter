package com.jremoter.core.toolkit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点标注注解
 * @author koko
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {
	
	public String value() default "";//当前扩展点默认的名称
	public ExtensionScope scope() default ExtensionScope.Singleton;//当前扩展点默认实例化方式 单例
	
}