package com.jremoter.core.plugin;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.toolkit.Extension;

@Extension
public interface PluginManager {
	
	public void addPlugin(Plugin plugin);
	
	public void start(ApplicationContext applicationContext,BeanContainer beanContainer,PackageScanner packageScanner);
	
	public void stop(ApplicationContext applicationContext,BeanContainer beanContainer);
	
}