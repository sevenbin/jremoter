package com.jremoter.core.context;

import com.jremoter.core.BootStrap;
import com.jremoter.core.annotation.Configuration;
import com.jremoter.core.annotation.JRemoterApplication;

@JRemoterApplication
@Configuration
public class TestBootStrap {
	
	public static void main(String[] args) {
		BootStrap.run(TestBootStrap.class);
	}
	
}