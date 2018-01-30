package com.jremoter.core.util;

import org.junit.Test;

import com.jremoter.core.util.StringUtil;

public class TestStringUtil {
	
	@Test
	public void testIsBlank(){
		System.out.println(StringUtil.isBlank(null));
		System.out.println(StringUtil.isBlank(""));
		System.out.println(StringUtil.isBlank("	"));
		System.out.println(StringUtil.isBlank("		"));
		System.out.println(StringUtil.isBlank("a"));
		System.out.println(StringUtil.isBlank(" a"));
		System.out.println(StringUtil.isBlank("a "));
	}
	
	@Test
	public void testTokenToArray(){
		String temp = "aaa,a,d,as,";
		System.out.println(StringUtil.tokenToArray(temp,",").length);
		for(String value : StringUtil.tokenToArray(temp,",")){
			System.out.println(value);
		}
	}
	
}