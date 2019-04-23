package com.xfrj.core.security;

/**
 * 算法工具类
 */
public class SecurityUtil {

	public static String encryptPassword(String password) {
		return encodeMD5(encodeSHA256(password));
	}
	
	public static String encodeBase64(byte[] data) {
		return BSUtil.toString(BaseCoder.encodeBase64(data));
	}
	
	public static String encodeMD5(String data){
		return encodeBase64(MDCoder.encodeMD5(data));
	}
	
	public static String encodeSHA256(String data){
		return encodeBase64(SHACoder.encodeSHA256(data));
	}
	
	public static void main(String[] args) {
		try {
			String pw = "123456";
			System.out.println("encryptPassword：" + encryptPassword(pw));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
