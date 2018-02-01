package com.jremoter.core.context;

import java.io.Closeable;
import java.util.Map;

public interface ApplicationContext extends Closeable{
	
	//刷新
	public void refresh();
	
	//获取指定的对象
	public <T> T getBean(Class<?> type,String beanName);
	
	//获取指定类型的对象清单
	public <T> Map<String,T> getBeans(Class<?> type);
	
}