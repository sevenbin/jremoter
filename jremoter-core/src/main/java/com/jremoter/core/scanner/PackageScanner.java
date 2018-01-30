package com.jremoter.core.scanner;

import java.util.Set;

import com.jremoter.core.pattern.PatternMatcher;
import com.jremoter.core.toolkit.Extension;
import com.jremoter.core.toolkit.ExtensionScope;

/**
 * 包扫描器
 * @author koko
 *
 */
@Extension(scope=ExtensionScope.Prototype)
public interface PackageScanner {
	
	public PackageScannerHandlerChain getPackageScannerHandlerChain();
	
	public void addPattern(String... patterns);
	
	public Set<Class<?>> scan(PatternMatcher patternMatcher);
	
}