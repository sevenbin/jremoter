package com.jremoter.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 字符串处理工具类
 * @author koko
 */
public abstract class StringUtil {
	
	private StringUtil(){}
	
	/**
	 * 字符串为空判断
	 * null -> true
	 * "" 	-> true
	 * " " 	-> true
	 * "a"	-> false
	 * " a"	-> false
	 * "a " -> false
	 * @param value	需要判断的值
	 * @return	true为空,false不为空
	 */
	public static boolean isBlank(String value){
		if(null == value || value.trim().length() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotBlank(String value){
		return !isBlank(value);
	}
	
	/**
	 * 集合类转String数组,null或empty返回0长度数组
	 * @param collection
	 * @return 返回拆分后的字符串数组,空为0长度数组
	 */
	public static String[] toArray(Collection<String> collection){
		if(null == collection || collection.size() == 0){
			return new String[0];
		}
		return collection.toArray(new String[collection.size()]);
	}
	
	/**
	 * 指定分隔符打断字符串为数组
	 * @param value
	 * @param delimiters
	 * @param trimToken
	 * @param ignoreEmptyToken
	 * @return 返回拆分后的字符串数组,空为0长度数组
	 */
	public static String[] tokenToArray(String value,String delimiters,boolean trimToken,boolean ignoreEmptyToken){
		if(null == value){
			return new String[0];
		}
		StringTokenizer stringTokenizer = new StringTokenizer(value,delimiters);
		List<String> tokens = new ArrayList<String>();
		while(stringTokenizer.hasMoreTokens()){
			String token = stringTokenizer.nextToken();
			if(trimToken){
				token = token.trim();
			}
			if(!ignoreEmptyToken || token.length() > 0){
				tokens.add(token);
			}
		}
		return toArray(tokens);
	}
	
	public static String[] tokenToArray(String value,String delimiters){
		return tokenToArray(value,delimiters,true,true);
	}
	
}