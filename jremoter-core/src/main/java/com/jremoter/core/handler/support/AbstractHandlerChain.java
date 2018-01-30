/**   
 * @title: AbstractHandlerChain.java 
 * @package com.jremoter.core.handler.support 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月23日 下午2:09:07 
 * @version 1.0.0
 * @description: 抽象的handler链管理器
 */
package com.jremoter.core.handler.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jremoter.core.handler.Handler;
import com.jremoter.core.handler.HandlerChain;
import com.jremoter.core.handler.HandlerContext;

public abstract class AbstractHandlerChain<T extends Handler> implements HandlerChain<T>{
	
	protected HandlerContext<T> head;
	protected HandlerContext<T> foot;
	
	public AbstractHandlerChain(){
		this.head = this.createHeadHandlerContext();
		this.foot = this.createFootHandlerContext();
		this.head.setNext(this.foot);
		this.foot.setPrev(this.head);
	}
	
	@Override
	public Iterator<Entry<String, T>> iterator() {
		return this.getHandlersAsMap().entrySet().iterator();
	}
	
	@Override
	public void addFirst(String name,T handler){
		final HandlerContext<T> context;
		synchronized(this){
			context = this.createHandlerContext(name,handler);
			this.doAddFirst(context);
		}
	}

	@Override
	public void addLast(String name,T handler){
		final HandlerContext<T> context;
		synchronized(this){
			context = this.createHandlerContext(name,handler);
			this.doAddLast(context);
		}
	}

	@Override
	public void addBefore(String target,String name,T handler){
		final HandlerContext<T> nContext;
		final HandlerContext<T> oContext;
		synchronized(this){
			oContext = this.getContext(target);
			nContext = this.createHandlerContext(name,handler);
			if(null == oContext){
				throw new IllegalArgumentException("old context not found : [ "+ target +"]");
			}
			this.doAddBefore(nContext,oContext);
		}
	}

	@Override
	public void addAfter(String target,String name,T handler){
		final HandlerContext<T> nContext;
		final HandlerContext<T> oContext;
		synchronized(this){
			oContext = this.getContext(target);
			nContext = this.createHandlerContext(name,handler);
			if(null == oContext){
				throw new IllegalArgumentException("old context not found : [ "+ target +"]");
			}
			this.doAddAfter(nContext,oContext);
		}
	}

	@Override
	public void removeHandler(String target){
		HandlerContext<T> context = this.getContext(target);
		context.getPrev().setNext(context.getNext());
		context.getNext().setPrev(context.getPrev());
	}
	
	@Override
	public boolean containsHandler(String target){
		HandlerContext<T> context = this.head.getNext();
		while(context != this.foot){
			if(context.getName().equals(target)){
				return true;
			}
			context = context.getNext();
		}
		return false;
	}

	@Override
	public void replaceHandler(String target, String name, T handler){
		HandlerContext<T> context = this.getContext(target);
		HandlerContext<T> newContext = this.createHandlerContext(name,handler);
		newContext.setPrev(context.getPrev());
		newContext.setNext(context.getNext());
	}

	@Override
	public T getFirstHandler(){
		HandlerContext<T> context = this.getFirstHandlerContext();
		if(null != context){
			return context.getHandler();
		}
		return null;
	}

	@Override
	public T getLastHandler(){
		HandlerContext<T> context = this.getLastHandlerContext();
		if(null != context){
			return context.getHandler();
		}
		return null;
	}

	@Override
	public HandlerContext<T> getFirstHandlerContext(){
		HandlerContext<T> context = this.head.getNext();
		if(context == this.foot){
			return null;
		}
		return this.head.getNext();
	}

	@Override
	public HandlerContext<T> getLastHandlerContext(){
		HandlerContext<T> context = this.foot.getPrev();
		if(context == this.head){
			return null;
		}
		return this.foot.getPrev();
	}

	@Override
	public Map<String,T> getHandlersAsMap(){
		Map<String,T> result = new LinkedHashMap<String,T>();
		HandlerContext<T> context = this.head.getNext();
		for(;;){
			if(this.foot == context){
				return result;
			}
			result.put(context.getName(),context.getHandler());
			context = context.getNext();
		}
	}

	@Override
	public List<T> getHandlersAsList(){
		List<T> result = new ArrayList<T>();
		HandlerContext<T> context = this.head.getNext();
		for(;;){
			if(this.foot == context){
				return result;
			}
			result.add(context.getHandler());
			context = context.getNext();
		}
	}

	@Override
	public List<String> getHandlerNames(){
		List<String> result = new ArrayList<String>();
		HandlerContext<T> context = this.head.getNext();
		for(;;){
			if(this.foot == context){
				return result;
			}
			result.add(context.getName());
			context = context.getNext();
		}
	}
	
	private void doAddFirst(HandlerContext<T> newContext){
		HandlerContext<T> nextContext = this.head.getNext();
		newContext.setPrev(this.head);
		newContext.setNext(nextContext);
		this.head.setNext(newContext);
		nextContext.setPrev(newContext);
	}
	
	private void doAddLast(HandlerContext<T> newContext){
		HandlerContext<T> prevContext = this.foot.getPrev();
		newContext.setPrev(prevContext);
		newContext.setNext(this.foot);
		prevContext.setNext(newContext);
		this.foot.setPrev(newContext);
	}
	
	private void doAddBefore(HandlerContext<T> newContext,HandlerContext<T> oldContext){
		newContext.setPrev(oldContext.getPrev());
		newContext.setNext(oldContext);
		oldContext.getPrev().setNext(newContext);
		oldContext.setPrev(newContext);
	}
	
	private void doAddAfter(HandlerContext<T> newContext,HandlerContext<T> oldContext){
		newContext.setPrev(oldContext);
		newContext.setNext(oldContext.getNext());
		oldContext.getNext().setPrev(newContext);
		oldContext.setNext(newContext);
	}
	
	private HandlerContext<T> getContext(String name){
		HandlerContext<T> context = this.head.getNext();
		while(context != this.foot){
			if(context.getName().equals(name)){
				return context;
			}
			context = context.getNext();
		}
		return null;
	}
	
	protected abstract HandlerContext<T> createHeadHandlerContext();
	protected abstract HandlerContext<T> createFootHandlerContext();
	protected abstract HandlerContext<T> createHandlerContext(String name,T handler);
	
}