package com.xfrj.core.utils;

import java.util.List;
import java.util.Map;

/**
 * 通用工具类
 */
public class CommonUtil {
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
}
