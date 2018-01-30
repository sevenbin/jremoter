package com.jremoter.core.scanner;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.scanner.support.DefaultPackageScannerHandler;
import com.jremoter.core.toolkit.ExtensionLoader;

public class TestDefaultPackageScanner {
	
	@Test
	public void testScanning(){
		
		PatternMatcher patternMatcher = ExtensionLoader.getService(PatternMatcher.class,"package");
		PackageScanner packageScanner = ExtensionLoader.getService(PackageScanner.class,"default");
		packageScanner.addPattern("com");
		packageScanner.getPackageScannerHandlerChain().addLast("interface",new DefaultPackageScannerHandler(){
			@Override
			public boolean onClassFound(Class<?> type){
				if(type.isInterface() || Modifier.isAbstract(type.getModifiers())){
					return false;
				}
				return true;
			}
		});
		packageScanner.getPackageScannerHandlerChain().addLast("annotation",new DefaultPackageScannerHandler(){
			@Override
			public boolean onClassFound(Class<?> type){
				if(type.getSimpleName().equals("LoggerFormatter")){
					return true;
				}else{
					return false;
				}
			}
		});
		System.out.println(packageScanner);
		for(Class<?> type : packageScanner.scan(patternMatcher)){
			System.out.println(type);
		}
		
	}
	
}