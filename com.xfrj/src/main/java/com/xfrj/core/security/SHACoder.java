package com.xfrj.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xfrj.core.security.ArithmeticContant.SHAEnum;

/**
 * SHA算法
 */
public class SHACoder {

	/**
	 * SHA256加密
	 * @param data
	 * @return
	 */
	public static byte[] encodeSHA256(String data) {
		return encodeSHA256(BSUtil.toBytes(data), SHAEnum.SHA_256);
 	}
	
	/**
	 * SHA加密
	 * @param data
	 * @param arithmetic
	 * @return
	 */
	public static byte[] encodeSHA256(byte[] data, SHAEnum arithmetic) {
		try {
			return MessageDigest.getInstance(arithmetic.getKey()).digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(arithmetic.getKey() + "加密失败", e);
		}
	}
}
