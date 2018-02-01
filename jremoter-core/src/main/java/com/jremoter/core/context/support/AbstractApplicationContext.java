package com.jremoter.core.context.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jremoter.core.Constant;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerFactory;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.option.Configuration;
import com.jremoter.core.option.support.AbstractConfiguration;
import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.scanner.support.DefaultPackageScannerHandler;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.util.ClassUtil;

public abstract class AbstractApplicationContext extends DefaultPackageScannerHandler implements ApplicationContext{
	
	protected Configuration configuration;
	protected Class<?> runner;
	protected BeanContainer beanContainer;
	
	public AbstractApplicationContext(Class<?> runner){
		this.runner = runner;
	}
	
	@Override
	public void refresh(){
		this.configuration = AbstractConfiguration.getConfiguration();
		BeanContainerFactory beanContainerFactory = ExtensionLoader.getService(BeanContainerFactory.class,configuration.getOption(Constant.O_BEAN_CONTAINER_FACTORY));
		
		PatternMatcher patternMatcher = ExtensionLoader.getService(PatternMatcher.class,configuration.getOption(Constant.O_PACKAGE_PATTERN_MATCHER));
		
		this.beanContainer = beanContainerFactory.createBeanContainer(this);
		
		Set<String> parrerns = this.searchConfigurationPatterns(this.runner.getPackage().getName());
		
		PackageScanner packageScanner = this.createPackageScanner();
		packageScanner.addPattern(this.runner.getPackage().getName());
		packageScanner.addPattern(parrerns.toArray(new String[parrerns.size()]));
		packageScanner.getPackageScannerHandlerChain().addLast("scanner",this);
		
		this.beanContainer.attachBean(this.runner,BeanScope.Singleton,ClassUtil.getCamelClassName(this.runner));
		for(Class<?> type : packageScanner.scan(patternMatcher)){
			String beanName = this.getBeanName(type);
			BeanScope beanScope = this.getBeanScope(type);
			this.beanContainer.attachBean(type,beanScope,beanName);
		}
		
		this.beanContainer.initial();
		
	}
	
	@Override
	public void close() throws IOException{
		if(null != this.beanContainer){
			this.beanContainer.destory();
		}
	}

	@Override
	public <T> T getBean(Class<?> type, String beanName){
		if(null == type){
			return null;
		}
		BeanDefinition beanDefinition = this.beanContainer.getBeanDefinition(type,beanName);
		if(null == beanDefinition){
			return null;
		}
		return beanDefinition.getBeanInstance();
	}
	
	@Override
	public <T> Map<String, T> getBeans(Class<?> type){
		Map<String,T> result = new HashMap<String,T>();
		if(null == type){
			return result;
		}
		for(Entry<String,BeanDefinition> entry : this.beanContainer.getBeanDefinitions(type).entrySet()){
			T object = entry.getValue().getBeanInstance();
			result.put(entry.getKey(),object);
		}
		return result;
	}
	
	protected abstract BeanScope getBeanScope(Class<?> type);
	protected abstract String getBeanName(Class<?> type);
	protected abstract PackageScanner createPackageScanner();
	protected abstract Set<String> searchConfigurationPatterns(String packagePattern);
	
}