package com.jremoter.core.pattern;

import java.util.Comparator;
import java.util.Map;

import com.jremoter.core.toolkit.Extension;
import com.jremoter.core.toolkit.ExtensionScope;

/**
 * 路径匹配器
 * @author koko
 *
 */
@Extension(scope=ExtensionScope.Prototype)
public interface PatternMatcher {
	
	public boolean isPattern(String path);
	
	public boolean match(String pattern,String path);
	
	public boolean matchStart(String pattern,String path);
	
	public String extractPathWithinPattern(String pattern,String path);
	
	public Map<String, String> extractUriTemplateVariables(String pattern, String path);
	
	public Comparator<String> getPatternComparator(String path);
	
	public String combine(String pattern1,String pattern2);	
	
}