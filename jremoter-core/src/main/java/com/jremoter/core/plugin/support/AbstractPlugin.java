/**   
 * @title: AbstractPlugin.java 
 * @package com.jremoter.core.plugin.support 
 * @author TangBin tb_bin@163.com
 * @date 2018年1月24日 上午10:49:40 
 * @version 1.0.0
 * @description: 抽象Plugin,可忽略排序,使用默认的序号
 */
package com.jremoter.core.plugin.support;

import com.jremoter.core.plugin.Plugin;

public abstract class AbstractPlugin implements Plugin{

	@Override
	public int sort() {
		return 100;
	}
	
}