package com.jremoter.core.context.support;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import com.jremoter.core.Constant;
import com.jremoter.core.annotation.Configuration;
import com.jremoter.core.annotation.Service;
import com.jremoter.core.bean.BeanScope;
import com.jremoter.core.logging.Logger;
import com.jremoter.core.logging.LoggerFactory;
import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.StringUtil;

public class AnnotationApplicationContext extends AbstractApplicationContext{
	
	private static final Logger log = LoggerFactory.getLogger(AnnotationApplicationContext.class);
	
	public AnnotationApplicationContext(Class<?> runner){
		super(runner);
	}
	
	@Override
	protected BeanScope getBeanScope(Class<?> type){
		if(AnnotationUtil.hasAnnotation(type,Service.class)){
			Service service = AnnotationUtil.getAnnotation(type,Service.class);
			return service.scope();
		}
		return BeanScope.Singleton;
	}

	@Override
	protected String getBeanName(Class<?> type){
		if(AnnotationUtil.hasAnnotation(type,Service.class)){
			Service service = AnnotationUtil.getAnnotation(type,Service.class);
			return service.value();
		}
		return null;
	}
	
	@Override
	public boolean onClassFound(Class<?> type){
		if(type.isInterface() || Modifier.isAbstract(type.getModifiers())){
			return false;
		}
		if(AnnotationUtil.hasAnnotation(type,Service.class) || AnnotationUtil.hasAnnotation(type,Configuration.class)){
			return true;
		}
		return false;
	}
	
	@Override
	protected PackageScanner createPackageScanner(){
		PackageScanner packageScanner = ExtensionLoader.getService(PackageScanner.class,this.configuration.getOption(Constant.O_PACKAGE_SCANNER));
		return packageScanner;
	}

	@Override
	protected Set<String> searchConfigurationPatterns(String packagePattern,Set<String> history){
		Set<String> parrerns = new LinkedHashSet<String>();
		history.add(packagePattern);
		log.debug("scan {} package ",packagePattern);
		PatternMatcher patternMatcher = ExtensionLoader.getService(PatternMatcher.class,configuration.getOption(Constant.O_PACKAGE_PATTERN_MATCHER));
		PackageScanner configPackageScanner = ExtensionLoader.getService(PackageScanner.class,this.configuration.getOption(Constant.O_PACKAGE_SCANNER));
		configPackageScanner.addPattern(packagePattern);
		
		for(Class<?> type : configPackageScanner.scan(patternMatcher)){
			com.jremoter.core.annotation.Configuration configuration = AnnotationUtil.getAnnotation(type,com.jremoter.core.annotation.Configuration.class);
			if(null == configuration){
				continue;
			}
			for(String pattern : configuration.patterns()){
				if(StringUtil.isBlank(pattern) || history.contains(pattern)){
					continue;
				}
				parrerns.add(pattern);
				parrerns.addAll(this.searchConfigurationPatterns(pattern,history));
			}
		}
		return parrerns;
	}
	
}