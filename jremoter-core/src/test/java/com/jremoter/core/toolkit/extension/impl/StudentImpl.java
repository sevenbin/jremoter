package com.jremoter.core.toolkit.extension.impl;

import com.jremoter.core.toolkit.ExtensionName;
import com.jremoter.core.toolkit.extension.IMember;

@ExtensionName("student")
public class StudentImpl implements IMember{

	@Override
	public String getName() {
		return "StudentImpl";
	}
	
}