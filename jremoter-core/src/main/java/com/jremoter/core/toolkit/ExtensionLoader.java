package com.jremoter.core.toolkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jremoter.core.util.AnnotationUtil;
import com.jremoter.core.util.ClassUtil;
import com.jremoter.core.util.GenericTypeUtil;
import com.jremoter.core.util.ResourceUtil;
import com.jremoter.core.util.ResourceUtil.ResourceMatcher;
import com.jremoter.core.util.StringUtil;

public class ExtensionLoader<T> {
	
	//扩展点缓存容器
	private static final ConcurrentHashMap<String,ExtensionLoader<?>> extensions = new ConcurrentHashMap<String,ExtensionLoader<?>>();
	//默认搜索配置文件的路径
	private static final String[] paths = new String[]{"META-INF/services/","META-INF/internal/"};
	
	private final Class<T> type;
	private final String name;
	private final ExtensionScope scope;
	private final ConcurrentHashMap<String,Class<T>> classes;
	private final ConcurrentHashMap<String,T> instances;
	
	private ExtensionLoader(Class<T> type){
		if(null == type){
			throw new IllegalArgumentException("type is null");
		}
		if(!type.isInterface()){
			throw new IllegalArgumentException("type must interface");
		}
		if(!AnnotationUtil.hasAnnotation(type,Extension.class)){
			throw new IllegalArgumentException("type has no @Extension annotation");
		}
		Extension spi = AnnotationUtil.getAnnotation(type,Extension.class);
		this.classes = new ConcurrentHashMap<String,Class<T>>();
		this.instances = new ConcurrentHashMap<String,T>();
		this.type = type;
		this.name = StringUtil.isBlank(spi.value()) ? null : spi.value();
		this.scope = spi.scope();
		this.refresh();
	}
	
	private void refresh(){
		Map<String,Class<T>> classes = new HashMap<String,Class<T>>();
		for(String path : paths){
			this.loadFile(path,classes);
		}
		this.classes.putAll(classes);
	}
	
	private void loadFile(String path,Map<String,Class<T>> classes){
		String filePath = String.format("%s%s",path,this.type.getName());
		ExtensionResourceMatcher matcher = new ExtensionResourceMatcher();
		Set<URL> urls = ResourceUtil.search(matcher,filePath);
		for(URL url : urls){
			try{
				InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String line = null;
				String[] temp = null;
				String name = null;
				String clazz = null;
				int index = -1;
				while((line = bufferedReader.readLine()) != null){
					if(StringUtil.isBlank(line)){
						continue;
					}
					index = line.indexOf("=");
					if(index > 0){
						temp = line.split("=");
						name = temp[0].trim();
						clazz = temp[1].trim();
						if(StringUtil.isBlank(clazz)){
							continue;
						}
						if(StringUtil.isBlank(name)){
							name = clazz;
						}
					}else{
						name = clazz = line;
					}
					try{
						Class<T> c = ClassUtil.forName(clazz,ExtensionLoader.class.getClassLoader()); 
						if(this.type.isAssignableFrom(c)){
							ExtensionName metaName = c.getAnnotation(ExtensionName.class);
							if(null != metaName){
								name = metaName.value();
							}
							classes.put(name,c);
						}else{
							throw new IllegalArgumentException(clazz + " must implements " + this.type.getName());
						}
					}finally{
						temp = null;
						index = -1;
						name = null;
						clazz = null;
					}
				}
				bufferedReader.close();
				inputStreamReader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 实例化指定class,注意:必须包含无参构造函数,否则无法实例化
	 * @param clazz
	 * @return 返回指定class的实例
	 */
	private T createInstance(Class<T> clazz){
		T result = null;
		try{
			result = clazz.newInstance();
		}catch(Throwable t){
			t.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据扩展点名称获取具体实例
	 * @param name
	 * @return 返回指定名称的扩展点实例
	 */
	public T getService(String name){
		if(StringUtil.isBlank(name)){
			name = this.name;
		}
		if(StringUtil.isBlank(name)){
			return null;
		}
		Class<T> clazz = classes.get(name);
		if(null == clazz){
			return null;
		}
		if(this.scope == ExtensionScope.Prototype){//这里需要根据注解的scope区分出实例化的方式
			return this.createInstance(clazz);
		}
		T result = instances.get(name);
		if(null == result){
			result = this.createInstance(clazz);
			if(null == result){
				return null;
			}
			instances.put(name,result);
		}
		return result;
	}
	
	public List<T> getServices(){
		List<T> result = new ArrayList<T>();
		for(Entry<String,Class<T>> entry : classes.entrySet()){
			if(this.type.isAssignableFrom(entry.getValue())){
				result.add(this.getService(entry.getKey()));
			}
		}
		return result;
	}
	
	/**
	 * 获取指定接口的ServideLoader实例,存在为空的可能
	 * @param type
	 * @return 返回指定接口的ServiceLoader
	 */
	public static <E> ExtensionLoader<E> getExtensionLoader(Class<E> type){
		if(null == type){
			throw new IllegalArgumentException("type is null");
		}
		String name = type.getName();
		ExtensionLoader<E> serviceLoader = null;
		if(!extensions.containsKey(name)){
			extensions.putIfAbsent(name,new ExtensionLoader<E>(type));
		}
		serviceLoader = GenericTypeUtil.parseType(extensions.get(name));
		return serviceLoader;
	}
	
	/**
	 * 快捷方法,直接获取指定接口的扩展点实例
	 * @param type
	 * @param name
	 * @return 返回指定类型和名称的扩展点实例
	 */
	public static <E> E getService(Class<E> type,String name){
		ExtensionLoader<E> serviceLoader = getExtensionLoader(type);
		if(null == serviceLoader){
			throw new IllegalArgumentException("ExtensionLoader not found");
		}
		return serviceLoader.getService(name);
	}
	
	private static class ExtensionResourceMatcher implements ResourceMatcher{
		@Override
		public boolean matcher(URL url){
			return true;
		}
	}
	
}