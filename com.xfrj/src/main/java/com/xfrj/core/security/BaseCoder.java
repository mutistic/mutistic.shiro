package com.xfrj.core.security;

import java.util.Base64;

/**
 * Base算法
 */
public class BaseCoder {

	/**
	 * 1、jdk1.8 java.util.Base64.java api
	 * https://blog.fondme.cn/apidoc/jdk-1.8-google/java/util/Base64.html
	 */
	
	/**
	 * Base64加密
	 * @param data
	 * @return
	 */
	public static byte[] encodeBase64(byte[] data) {
		return Base64.getEncoder().encode(data);
	}
	
	/**
	 * Base64解密
	 * @param data
	 * @return
	 */
	public static byte[] decodeBase64(byte[] data) {
		return Base64.getDecoder().decode(data);
	}
	
}
