package com.jremoter.core.pattern.support;

import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("package")
public class PackagePatternMatcher extends AbstractPatternMatcher{

	public PackagePatternMatcher() {
		super(".");
	}
	
}