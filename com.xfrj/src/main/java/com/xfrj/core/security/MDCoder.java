package com.xfrj.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDCoder {

	public static byte[] encodeMD5(String data) {
		return encodeMD5(BSUtil.toBytes(data));
	}
	
	public static byte[] encodeMD5(byte[] data) {
		try {
			return MessageDigest.getInstance(ArithmeticEnum.MD5.getKey()).digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(ArithmeticEnum.MD5.getKey() + "加密失败", e);
		}
	}
}
