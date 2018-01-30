package com.jremoter.core.handler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 业务处理句柄链
 * @author koko
 * 
 */
public interface HandlerChain<T extends Handler> extends Iterable<Entry<String,T>>  {
	
	public void addFirst(String name,T handler);
	public void addLast(String name,T handler);
	public void addBefore(String target,String name,T handler);
	public void addAfter(String target,String name,T handler);
	
	public void removeHandler(String target);
	public boolean containsHandler(String target);
	public void replaceHandler(String target,String name,T handler);
	
	public T getFirstHandler();
	public T getLastHandler();
	
	public HandlerContext<T> getFirstHandlerContext();
	public HandlerContext<T> getLastHandlerContext();
	
	public Map<String,T> getHandlersAsMap();
	public List<T> getHandlersAsList();
	public List<String> getHandlerNames();
	
}