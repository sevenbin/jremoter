package com.jremoter.core.bean;

/**
 * Bean实例化方式
 * @author koko
 *
 */
public enum BeanScope {
	
	Prototype("prototype"),//多例
	Singleton("singleton"); //单例
	
	private String value;
	
	public String getValue(){
		return this.value;
	}
	
	BeanScope(String value){
		this.value = value;
	}
	
	public static BeanScope parse(String value){
		for(BeanScope scope : BeanScope.values()){
			if(scope.getValue().equals(value)){
				return scope;
			}
		}
		return null;
	}
	
}