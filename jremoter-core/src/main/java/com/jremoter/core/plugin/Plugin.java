package com.jremoter.core.plugin;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.toolkit.Extension;
import com.jremoter.core.toolkit.ExtensionScope;

@Extension(scope=ExtensionScope.Prototype)
public interface Plugin{
	
	public int sort();
	
	public void start(ApplicationContext applicationContext,BeanContainer beanContainer,PackageScanner packageScanner);
	
	public void stop(ApplicationContext applicationContext,BeanContainer beanContainer);
	
}