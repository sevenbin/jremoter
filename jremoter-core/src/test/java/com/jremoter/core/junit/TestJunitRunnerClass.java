package com.jremoter.core.junit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.jremoter.core.annotation.JRemoterApplication;
import com.jremoter.core.annotation.Value;

@JRemoterApplication(name="demo")
@RunWith(JRemoterJunitForClassRunner.class)
public class TestJunitRunnerClass {
	
	@Value("demo.name")
	private String name;
	
	@Test
	public void testRun(){
		System.out.println(this);
		System.out.println(this.name);
	}
	
}