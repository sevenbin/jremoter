package com.jremoter.core.pattern.support;

import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("resource")
public class ResourcePatternMatcher extends AbstractPatternMatcher{
	
	public ResourcePatternMatcher() {
		super("/");
	}
	
}