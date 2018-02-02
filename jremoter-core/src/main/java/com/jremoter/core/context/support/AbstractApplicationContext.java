package com.jremoter.core.context.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jremoter.core.Constant;
import com.jremoter.core.annotation.JRemoterApplication;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.bean.BeanContainerFactory;
import com.jremoter.core.bean.BeanDefinition;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.context.ApplicationContextBanner;
import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;
import com.jremoter.core.option.Configuration;
import com.jremoter.core.option.support.AbstractConfiguration;
import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.plugin.PluginManager;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.scanner.support.DefaultPackageScannerHandler;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ClassUtil;

public abstract class AbstractApplicationContext extends DefaultPackageScannerHandler implements ApplicationContext{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractApplicationContext.class);
	
	protected Configuration configuration;
	protected Class<?> runner;
	protected BeanContainerFactory beanContainerFactory;
	protected BeanContainer beanContainer;
	protected ApplicationContextBanner banner;
	protected PluginManager pluginManager;
	
	public AbstractApplicationContext(Class<?> runner){
		if(null == runner){
			throw new NullPointerException("runner");
		}
		if(!AnnotationUtil.hasAnnotation(runner,JRemoterApplication.class)){
			throw new IllegalArgumentException("runner class missing @JRemoterApplication annotation");
		}
		JRemoterApplication jRemoterApplication = AnnotationUtil.getAnnotation(runner,JRemoterApplication.class);
		System.setProperty(Constant.K_CONFIGURATION_NAME,jRemoterApplication.name());
		System.setProperty(Constant.K_CONFIGURATION_PATH,jRemoterApplication.path());
		this.runner = runner;
		this.configuration = AbstractConfiguration.getConfiguration();
		this.banner = ExtensionLoader.getService(ApplicationContextBanner.class,this.configuration.getOption(Constant.O_BANNER));
		this.banner.write(System.out);
		this.beanContainerFactory = ExtensionLoader.getService(BeanContainerFactory.class,configuration.getOption(Constant.O_BEAN_CONTAINER_FACTORY));
		this.beanContainer = beanContainerFactory.createBeanContainer(this);
		this.pluginManager = ExtensionLoader.getService(PluginManager.class,this.configuration.getOption(Constant.O_PLUGIN_MANAGER));
		log.info("create application context success");
	}
	
	@Override
	public BeanContainer getBeanContainer() {
		return this.beanContainer;
	}

	@Override
	public void refresh(){
		PatternMatcher patternMatcher = ExtensionLoader.getService(PatternMatcher.class,configuration.getOption(Constant.O_PACKAGE_PATTERN_MATCHER));
		Set<String> history = new LinkedHashSet<String>();
		history.add(this.runner.getPackage().getName());
		Set<String> patterns = this.searchConfigurationPatterns(this.runner.getPackage().getName(),history);
		
		PackageScanner packageScanner = this.createPackageScanner();
		packageScanner.getPackageScannerHandlerChain().addLast("scanner",this);
		packageScanner.addPattern(this.runner.getPackage().getName());
		if(patterns.size() > 0){
			packageScanner.addPattern(patterns.toArray(new String[patterns.size()]));
		}
		
		this.beanContainer.attachBean(this.runner,BeanScope.Singleton,ClassUtil.getCamelClassName(this.runner));
		this.beanContainer.attachBean(ApplicationContext.class,ClassUtil.getCamelClassName(ApplicationContext.class),this);
		this.beanContainer.attachBean(BeanContainer.class,ClassUtil.getCamelClassName(BeanContainer.class),this.beanContainer);
		this.beanContainer.attachBean(Configuration.class,ClassUtil.getCamelClassName(Configuration.class),this.configuration);
		
		this.pluginManager.start(this,this.beanContainer,packageScanner);
		for(Class<?> type : packageScanner.scan(patternMatcher)){
			String beanName = this.getBeanName(type);
			BeanScope beanScope = this.getBeanScope(type);
			this.beanContainer.attachBean(type,beanScope,beanName);
		}
		
		this.beanContainer.initial();
		
	}
	
	@Override
	public void close() throws IOException{
		if(null != this.pluginManager){
			this.pluginManager.stop(this,this.beanContainer);
		}
		if(null != this.beanContainer){
			this.beanContainer.destory();
		}
		log.info("close application context success");
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
	protected abstract Set<String> searchConfigurationPatterns(String packagePattern,Set<String> history);
	
}