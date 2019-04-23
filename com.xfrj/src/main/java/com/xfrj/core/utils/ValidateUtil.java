package com.xfrj.core.utils;

import org.springframework.util.ObjectUtils;

/**
 * 验证工具类
 */
public class ValidateUtil {

	private final static String LEFT = "[";
	private final static String RIGHT = "]";
	private final static String NOT_NULL = "不能为空！";
	private final static String LENGTH = "长度不能超过";
	private final static String CHAR = "字符！";

	/**
	 * 抛出异常信息
	 * @param message
	 */
	public static void exception(String message) {
		throw new IllegalArgumentException(message);
	}
	/**
	 * 根据key抛出异常信息
	 * @param message
	 * @param key
	 */
	public static void exception(String message, Object key) {
		exception(key == null ? message : LEFT + key + RIGHT + message);
	}

	/**
	 * 非空校验
	 * @param obj
	 * @param message
	 */
	public static void notNull(Object obj, String message) {
		if (ObjectUtils.isEmpty(obj)) {
			exception(message + NOT_NULL);
		}
	}
	/**
	 * 字符串长度校验（不校验空）
	 * @param text
	 * @param max
	 * @param message
	 */
	public static void length(String text, Integer max, String message) {
		if (!ObjectUtils.isEmpty(text) && text.trim().length() > max.intValue()) {
			exception(message + LENGTH + max + CHAR);
		}
	}
	/**
	 * 字符串长度校验（校验非空）
	 * @param text
	 * @param max
	 * @param message
	 */
	public static void notLength(String text, Integer max, String message) {
		notNull(text, message + NOT_NULL);
		if (text.trim().length() > max.intValue()) {
			exception(message + LENGTH + max + CHAR);
		}
	}

}
