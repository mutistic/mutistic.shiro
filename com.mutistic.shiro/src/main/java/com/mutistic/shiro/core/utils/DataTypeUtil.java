package com.mutistic.shiro.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据类型判断工具类
 */
public class DataTypeUtil {

	/**
	 * 判断是否为数组、List、Set
	 * @param obj
	 * @return
	 */
	public static boolean isCollection(Object obj) {
		return obj != null && (obj.getClass().isArray() || obj instanceof List || obj instanceof Set); // List.class.isAssignableFrom(obj.getClass())
	}

	/**
	 * 判断是否为Map
	 * @param obj
	 * @return
	 */
	public static boolean isMap(Object obj) {
		return obj != null && (obj instanceof Map); // Map.class.isAssignableFrom(obj.getClass())
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		System.out.println(JSONObject.toJSON(new ArrayList()));
		System.out.println(JSONObject.toJSON(new LinkedList()));
		System.out.println(JSONObject.toJSON(new HashSet()));
		System.out.println(JSONObject.toJSON(new LinkedHashSet()));
		System.out.println(JSONObject.toJSON(new String[] {}));
		System.out.println(JSONObject.toJSON(new HashMap()));
		System.out.println(JSONObject.toJSON(new LinkedHashMap()));

		System.out.println(DataTypeUtil.isCollection(new ArrayList()));
		System.out.println(DataTypeUtil.isCollection(new LinkedList()));
		System.out.println(DataTypeUtil.isCollection(new HashSet()));
		System.out.println(DataTypeUtil.isCollection(new LinkedHashSet()));
		System.out.println(DataTypeUtil.isCollection(new String[] {}));
		System.out.println(DataTypeUtil.isMap(new HashMap()));
		System.out.println(DataTypeUtil.isMap(new LinkedHashMap()));
	}
}
