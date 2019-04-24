package com.xfrj.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.xfrj.core.security.ArithmeticContant.MDEnum;

/**
 * MD算法
 */
public class MDCoder {

	/**
	 * MD5加密
	 * @param data
	 * @return
	 */
	public static byte[] encodeMD5(String data) {
		return encodeMD5(BSUtil.toBytes(data), MDEnum.MD5);
	}
	
	/**
	 * MD加密
	 * @param data
	 * @param arithmetic
	 * @return
	 */
	public static byte[] encodeMD5(byte[] data, MDEnum arithmetic) {
		try {
			return MessageDigest.getInstance(arithmetic.getKey()).digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(arithmetic.getKey() + "加密失败", e);
		}
	}
}
