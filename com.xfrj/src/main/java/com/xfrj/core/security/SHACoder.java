package com.xfrj.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHACoder {

	public static byte[] encodeSHA256(String data) {
		return encodeSHA256(BSUtil.toBytes(data));
 	}
	
	public static byte[] encodeSHA256(byte[] data) {
		try {
			return MessageDigest.getInstance(ArithmeticEnum.SHA_256.getKey()).digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(ArithmeticEnum.SHA_256.getKey() + "加密失败", e);
		}
	}
}
