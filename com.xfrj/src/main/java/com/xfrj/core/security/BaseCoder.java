package com.xfrj.core.security;

import java.util.Base64;

/**
 * https://blog.fondme.cn/apidoc/jdk-1.8-google/java/util/Base64.html
 */
public class BaseCoder {

	public static byte[] encodeBase64(byte[] data) {
		return Base64.getEncoder().encode(data);
	}
	
	public static byte[] decodeBase64(byte[] data) {
		return Base64.getDecoder().decode(data);
	}
	
}
