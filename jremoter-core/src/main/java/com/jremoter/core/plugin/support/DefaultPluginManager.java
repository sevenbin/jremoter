/**   
 * @title: DefaultPluginManager.java 
 * @package com.jremoter.core.plugin.support 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月24日 上午10:38:09 
 * @version 1.0.0
 * @description: 默认的插件管理器
 */
package com.jremoter.core.plugin.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jremoter.core.bean.BeanContainer;
import com.jremoter.core.context.ApplicationContext;
import com.jremoter.core.plugin.Plugin;
import com.jremoter.core.plugin.PluginManager;
import com.jremoter.core.scanner.PackageScanner;
import com.jremoter.core.toolkit.ExtensionLoader;
import com.jremoter.core.toolkit.ExtensionName;

@ExtensionName("default")
public class DefaultPluginManager implements PluginManager{
	
	protected List<Plugin> plugins;
	
	public DefaultPluginManager(){
		this.plugins = new ArrayList<Plugin>();
		ExtensionLoader<Plugin> serviceLoader = ExtensionLoader.getExtensionLoader(Plugin.class);
		for(Plugin plugin : serviceLoader.getServices()){
			if(null != plugin){
				this.plugins.add(plugin);
			}
		}
		this.sortPlugin();
	}
	
	@Override
	public void addPlugin(Plugin plugin) {
		if(null == plugin){
			return;
		}
		this.plugins.add(plugin);
		this.sortPlugin();
	}

	@Override
	public void start(ApplicationContext applicationContext,BeanContainer beanContainer,PackageScanner packageScanner) {
		for(Plugin plugin : this.plugins){
			try{
				plugin.start(applicationContext,beanContainer,packageScanner);
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	@Override
	public void stop(ApplicationContext applicationContext,BeanContainer beanContainer) {
		for(Plugin plugin : this.plugins){
			try{
				plugin.stop(applicationContext, beanContainer);
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private void sortPlugin(){
		Collections.sort(this.plugins,new Comparator<Plugin>(){
			@Override
			public int compare(Plugin o1, Plugin o2) {
				return new Integer(o1.sort()).compareTo(o2.sort());
			}
		});
	}
	
}