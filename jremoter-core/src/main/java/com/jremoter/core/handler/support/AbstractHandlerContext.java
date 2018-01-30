/**   
 * @title: AbstractHandlerContext.java 
 * @package com.jremoter.core.handler.support 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月23日 下午2:05:43 
 * @version 1.0.0
 * @description: 抽象handler,管理handler上下文
 */
package com.jremoter.core.handler.support;

import com.jremoter.core.handler.Handler;
import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.handler.HandlerContext;

public abstract class AbstractHandlerContext<T extends Handler> implements HandlerContext<T>{
	
	protected final HandlerChain<T> chain;
	protected final String name;
	protected final T handler;
	
	protected volatile HandlerContext<T> next;
	protected volatile HandlerContext<T> prev;
	
	public AbstractHandlerContext(HandlerChain<T> chain,String name,T handler){
		this.chain = chain;
		this.name = name;
		this.handler = handler;
	}
	
	@Override
	public String getName(){
		return this.name;
	}

	@Override
	public T getHandler(){
		return this.handler;
	}

	@Override
	public HandlerContext<T> getNext(){
		return this.next;
	}

	@Override
	public HandlerContext<T> getPrev(){
		return this.prev;
	}

	@Override
	public void setNext(HandlerContext<T> next){
		this.next = next;
	}

	@Override
	public void setPrev(HandlerContext<T> prev){
		this.prev = prev;
	}
	
	@Override
	public HandlerChain<T> getChain(){
		return this.chain;
	}
	
}