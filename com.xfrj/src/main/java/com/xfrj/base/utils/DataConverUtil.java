package com.xfrj.base.utils;

public class DataConverUtil {

	public static String char2String(Object charObj) {
		if(charObj == null || !charObj.getClass().isArray()) {
			return null;
		}
		
		return toString((char[])charObj);
	}
	
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
		Object a = new char[] {'a', 'b'};
//		Object a = new Character[] {'a', 'b'};
//		Object a = new int[] {1,2};
		System.out.println(DataConverUtil.char2String(a));
	}
	
}
