package com.jremoter.core.handler;

/**
 * 业务处理句柄上下文
 * @author koko
 *
 * @param <T>
 */
public interface HandlerContext<T extends Handler> {
	
	public String getName();
	public T getHandler();
	
	public HandlerContext<T> getNext();
	public HandlerContext<T> getPrev();
	
	public void setNext(HandlerContext<T> next);
	public void setPrev(HandlerContext<T> prev);
	
	public HandlerChain<T> getChain();
	
}