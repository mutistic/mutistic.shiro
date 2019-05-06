package com.mutistic.shiro.core.security;

/**
 * 算法工具类
 */
public class SecurityUtil {
	/** 
	 * 1、JOSN在线加密/解密 
	 * https://www.sojson.com/encrypt.html
	 */
	
	/** 默认私钥：MDCoder.encodeMD5("mutistic") */
	private final static byte[] PRIVATE_KEY = new byte[]{123, 101, -55, 51, -24, -48, 116, -116, -51, 81, 63, -91, -86, 9, 37, 14};
	
	/**
	 * 密码加密规则
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String password) {
		return encodeMD5(encodeSHA256(password));
	}
	/**
	 * Base64加密
	 * @param data
	 * @return
	 */
	public static String encodeBase64(byte[] data) {
		return BSUtil.toString(BaseCoder.encodeBase64(data));
	}
	/**
	 * Base64解密
	 * @param data
	 * @return
	 */
	public static byte[] decodeBase64(String data) {
		return BaseCoder.decodeBase64(BSUtil.toBytes(data));
	}
	/**
	 * MD5加密
	 * @param data
	 * @return
	 */
	public static String encodeMD5(String data){
		return encodeBase64(MDCoder.encodeMD5(data));
	}
	/**
	 * SHA256加密
	 * @param data
	 * @return
	 */
	public static String encodeSHA256(String data){
		return encodeBase64(SHACoder.encodeSHA256(data));
	}
	/**
	 * DES加密
	 * @param data
	 * @return
	 */
	public static final String encryptDES(String data) {
		return encodeBase64(DESCoder.encrypt(data, PRIVATE_KEY));
	}
	/**
	 * DES解密
	 * @param data
	 * @return
	 */
	public static final String decryptDES(String data) {
		return  BSUtil.toString(DESCoder.decrypt(decodeBase64(data), PRIVATE_KEY));
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(SecurityUtil.encryptPassword("222"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
