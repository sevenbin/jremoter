package com.jremoter.core.util;

import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class ResourceUtil {
	
	private ResourceUtil(){}
	
	public static interface ResourceMatcher{
		
		public boolean matcher(URL url);
		
	}
	
	public static Set<URL> search(ResourceMatcher resourceMatcher,String...paths){
		Set<URL> result = new LinkedHashSet<URL>();
		if(null == paths || paths.length == 0 || null == resourceMatcher){
			return result;
		}
		for(String path : paths){
			if(StringUtil.isBlank(path)){
				continue;
			}
			result.addAll(search(resourceMatcher,path));
		}
		return result;
	}
	
	public static Set<URL> search(ResourceMatcher resourceMatcher,String path){
		Set<URL> result = new LinkedHashSet<URL>();
		if(null == path || StringUtil.isBlank(path) || null == resourceMatcher){
			return result;
		}
		ClassLoader classLoader = ResourceUtil.class.getClassLoader();
		if(null == classLoader){
			classLoader = ClassUtil.getDefaultClassLoader();
		}
		try{
			Enumeration<URL> enumeration = null;
			if(null == classLoader){
				enumeration = ClassLoader.getSystemResources(path);
			}else{
				enumeration = classLoader.getResources(path);
			}
			if(null == enumeration){
				return result;
			}
			while(enumeration.hasMoreElements()){
				URL url = enumeration.nextElement();
				if(resourceMatcher.matcher(url)){
					result.add(url);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
}