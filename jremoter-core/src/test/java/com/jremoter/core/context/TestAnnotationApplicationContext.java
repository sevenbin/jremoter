package com.jremoter.core.context;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.jremoter.core.annotation.Autowired;
import com.jremoter.core.annotation.Configuration;
import com.jremoter.core.annotation.DestoryMethod;
import com.jremoter.core.annotation.InitialMethod;
import com.jremoter.core.annotation.JRemoterApplication;
import com.jremoter.core.annotation.Service;
import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.support.AnnotationApplicationContext;
import com.jremoter.core.junit.JRemoterJunitForClassRunner;

@JRemoterApplication
@Configuration(patterns="com.jremoter.core")
@RunWith(JRemoterJunitForClassRunner.class)
public class TestAnnotationApplicationContext {
	
	@Test
	public void runApplicationContext() throws IOException{
		
		ApplicationContext applicationContext = new AnnotationApplicationContext(TestAnnotationApplicationContext.class);
		applicationContext.refresh();
		System.out.println(applicationContext);
		System.out.println(this);
		applicationContext.close();
		
	}
	
	@Service
	public DemoService demoService(){
		return new DemoService();
	}
	
	@Autowired
	private TestService testService;
	@Autowired
	private DemoService demoService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private BeanContainer beanContainer;
	@Autowired
	private com.jremoter.core.option.Configuration configuration;
	
	@InitialMethod
	public void init(){
		
		System.out.println(this.applicationContext);
		System.out.println(this.beanContainer);
		System.out.println(this.configuration);
		
		System.out.println(this.demoService.demo());
		System.out.println(this.testService.getName());
		System.out.println("init -> "+this);
	}
	
	@DestoryMethod
	public void dest(){
		System.out.println("dest -> "+this);
	}
	
}