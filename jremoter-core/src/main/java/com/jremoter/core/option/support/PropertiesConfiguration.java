package com.jremoter.core.option.support;

import java.io.InputStream;
import java.util.Properties;

import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("properties")
public class PropertiesConfiguration extends AbstractConfiguration{

	private static final Properties properties = new Properties();
	
	public PropertiesConfiguration(){
		String path = this.getConfigFile();
		try{
			InputStream inputStream = this.getClass().getResourceAsStream(path);
			if(null != inputStream){
				properties.load(inputStream);
				inputStream.close();
			}
		}catch(Exception e){}
	}
	
	@Override
	protected String getValue(String name) {
		return properties.getProperty(name);
	}
	
}