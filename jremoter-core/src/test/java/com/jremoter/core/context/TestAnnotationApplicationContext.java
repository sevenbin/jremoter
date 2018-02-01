package com.jremoter.core.context;

import java.io.IOException;

import org.junit.Test;

import com.jremoter.core.annotation.Configuration;
import com.jremoter.core.annotation.JRemoterApplication;
import com.jremoter.core.context.support.AnnotationApplicationContext;

@JRemoterApplication
@Configuration
public class TestAnnotationApplicationContext {
	
	@Test
	public void runApplicationContext() throws IOException{
		
		ApplicationContext applicationContext = new AnnotationApplicationContext(TestAnnotationApplicationContext.class);
		applicationContext.refresh();
		
		applicationContext.close();
		
	}
	
}