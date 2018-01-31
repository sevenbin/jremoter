package com.jremoter.core.option;

import java.io.Serializable;

import com.jremoter.core.util.StringUtil;

public class Option<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private T oldValue;
	private T newValue;
	
	private Option(String name,T defaultValue){
		if(StringUtil.isBlank(name)){
			throw new IllegalArgumentException("option name is null");
		}
		this.name = name;
		this.oldValue = defaultValue;
	}
	
	public String getName(){
		return this.name;
	}
	
	public T getValue(){
		return null == this.newValue ? this.oldValue : this.newValue;
	}
	
	public void setValue(T value){
		this.newValue = value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(null == obj){
			return false;
		}
		if(!(obj instanceof Option)){
			return false;
		}
		@SuppressWarnings("unchecked")
		Option<T> option = (Option<T>)obj;
		if(this.name.equals(option.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.name).append("=").append(this.getValue());
		return sb.toString();
	}
	
	public static <T> Option<T> create(String name,T value){
		return new Option<T>(name,value);
	}
	
}