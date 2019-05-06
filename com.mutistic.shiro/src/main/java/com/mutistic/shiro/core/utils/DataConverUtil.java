package com.mutistic.shiro.core.utils;

/**
 * 数据转换工具类
 */
public class DataConverUtil {

	/**
	 * char array To String
	 * @param charObj char数组
	 * @return
	 */
	public static String char2String(Object charObj) {
		if(charObj == null || !charObj.getClass().isArray()) {
			return null;
		}
		
		return toString((char[])charObj);
	}
	/**
	 * char array To String
	 * @param chars char数组
	 * @return
	 */
	public static String toString(char[] chars) {
		if(null == chars) {
			return null;
		}
		if(chars.length == 0) {
			return "";
		}
		return String.valueOf(chars);
	}
	
	public static void main(String[] args) {
		System.out.println(DataConverUtil.char2String(new char[] {'a', 'b'}));
		System.out.println(DataConverUtil.toString(new char[] {'a', 'b'}));
	}
	
}
