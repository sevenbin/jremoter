package com.jremoter.core;

import java.io.IOException;

import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.context.support.AnnotationApplicationContext;

public class BootStrap {
	
	private ApplicationContext applicationContext;
	
	private BootStrap(Class<?> runner){
		if(null == runner){
			throw new NullPointerException("runner");
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				try{
					if(null != applicationContext){
						applicationContext.close();
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
		this.applicationContext = new AnnotationApplicationContext(runner);
		this.applicationContext.refresh();
	}
	
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}
	
	public static BootStrap run(Class<?> runner){
		return new BootStrap(runner);
	}
	
}