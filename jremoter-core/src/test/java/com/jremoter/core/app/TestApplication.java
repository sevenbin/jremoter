package com.jremoter.core.app;

import com.jremoter.core.BootStrap;
import com.jremoter.core.annotation.JRemoterApplication;

@JRemoterApplication
public class TestApplication {
	
	public static void main(String[] args) {
		BootStrap.run(TestApplication.class);
	}
	
}