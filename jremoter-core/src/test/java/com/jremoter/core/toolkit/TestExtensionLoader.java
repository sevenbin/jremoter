package com.jremoter.core.toolkit;

import org.junit.Test;

import com.jremoter.core.toolkit.extension.IMember;

public class TestExtensionLoader {
	
	@Test
	public void testLoader(){
		
		IMember member = ExtensionLoader.getService(IMember.class,"teacher");
		System.out.println(member);
		System.out.println(ExtensionLoader.getService(IMember.class,"teacher"));
		System.out.println(member.getName());
		
		System.out.println(ExtensionLoader.getService(IMember.class,"student"));
		System.out.println(ExtensionLoader.getService(IMember.class,"student").getName());
		
	}
	
}