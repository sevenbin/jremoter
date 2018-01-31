package com.jremoter.core.option.support;

import java.util.concurrent.ConcurrentHashMap;

import com.jremoter.core.Constant;
import com.jremoter.core.option.Configuration;
import com.jremoter.core.option.Option;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.util.GenericTypeUtil;
import com.jremoter.core.util.ObjectUtil;
import com.jremoter.core.util.StringUtil;

public abstract class AbstractConfiguration implements Configuration{
	
	protected static final ConcurrentHashMap<String,Option<?>> options = new ConcurrentHashMap<String,Option<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOption(Option<T> option){
		if(null == option){
			return null;
		}
		String name = option.getName();
		Option<T> temp = GenericTypeUtil.parseType(options.get(name));
		if(null == temp){
			options.put(name,option);
			temp = GenericTypeUtil.parseType(options.get(name));
			String value = this.getValue(name);
			if(StringUtil.isNotBlank(value)){
				if(option.getValue() != null){
					temp.setValue((T)ObjectUtil.parseType(option.getValue().getClass(),value));
				}else{
					temp.setValue((T)value);
				}
			}
		}
		return temp.getValue();
	}

	@Override
	public <T> T getOption(String option){
		Option<T> optionObject = Option.create(option,null);
		return getOption(optionObject);
	}
	
	protected String getConfigFile(){
		String name = System.getProperty(Constant.K_CONFIGURATION_NAME,Constant.V_CONFIGURATION_NAME);
		String path = System.getProperty(Constant.K_CONFIGURATION_PATH,Constant.V_CONFIGURATION_PATH);
		return String.format("/%s.%s",name,path);
	}
	
	protected abstract String getValue(String name);
	
	public static Configuration getConfiguration(){
		String path = System.getProperty(Constant.K_CONFIGURATION_PATH,Constant.V_CONFIGURATION_PATH);
		Configuration configure = ExtensionLoader.getService(Configuration.class,path);
		if(null == configure){
			throw new IllegalStateException("IConfiguration not found : " + path);
		}
		return configure;
	}
	
}