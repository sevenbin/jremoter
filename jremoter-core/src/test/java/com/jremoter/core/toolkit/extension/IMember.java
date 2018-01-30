package com.jremoter.core.toolkit.extension;

import com.jremoter.core.toolkit.Extension;
import com.jremoter.core.toolkit.ExtensionScope;

@Extension(scope=ExtensionScope.Prototype)
public interface IMember {
	
	public String getName();
	
}