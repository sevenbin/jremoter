package com.jremoter.core.toolkit.extension.impl;

import com.jremoter.core.toolkit.ExtensionName;
import com.jremoter.core.toolkit.extension.IMember;

@ExtensionName("teacher")
public class TeacherImpl implements IMember{

	@Override
	public String getName() {
		return "TeacherImpl";
	}
	
}