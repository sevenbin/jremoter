package com.jremoter.core.app.plugin;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.plugin.support.AbstractPlugin;
import com.jremoter.core.scanner.PackageScanner;

public class ApplicationPlugin extends AbstractPlugin{

	@Override
	public void start(ApplicationContext applicationContext,BeanContainer beanContainer, PackageScanner packageScanner) {
		System.out.println("start");
	}

	@Override
	public void stop(ApplicationContext applicationContext,BeanContainer beanContainer) {
		System.out.println("stop");
	}
	
}