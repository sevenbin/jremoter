package com.jremoter.core;

import com.jremoter.core.option.Option;

public abstract class Constant {
	
	private Constant(){}
	
	//默认使用分隔符
	public static final String F_SEPARATOR = ",";
	
	//默认配置项名称与值
	public static final String K_CONFIGURATION_NAME = "configuration_name";
	public static final String K_CONFIGURATION_PATH = "configuration_path";
	public static final String V_CONFIGURATION_NAME = "application";
	public static final String V_CONFIGURATION_PATH = "properties";
	
	//包扫描器
	public static final Option<String> O_PACKAGE_SCANNER = Option.create("jremoter.package_scanner","default");
	//包匹配器
	public static final Option<String> O_PACKAGE_PATTERN_MATCHER = Option.create("jremoter.package_pattern_matcher","package");
	//路径匹配器
	public static final Option<String> O_RESOURCE_PATTERN_MATCHER = Option.create("jremoter.resource_pattern_matcher","resource");
	//代理工厂
	public static final Option<String> O_PROXY_FACTORY = Option.create("jremoter.proxy_factory","native");
	//Bean工厂
	public static final Option<String> O_BEAN_DEFINITION_FACTORY = Option.create("jremoter.bean_definition_factory","default");
	//Banner
	public static final Option<String> O_BANNER = Option.create("jremoter.banner","default");
	//Bean容器
	public static final Option<String> O_BEAN_CONTAINER_FACTORY = Option.create("jremoter.bean_container_factory","default");
	
}
