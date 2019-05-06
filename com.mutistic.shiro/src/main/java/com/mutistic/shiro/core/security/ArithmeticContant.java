package com.mutistic.shiro.core.security;

/**
 * 算法常量类
 */
public class ArithmeticContant {

	/** 
	 * 1、Java密码体系结构
	 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#alg
	 */
	/** BASE算法 */
	public enum BASEEnum {
		/** Base64 */
		BASE64("Base64"),
		;
		private String key;
		public String getKey() { return key; }
		private BASEEnum(String key) { this.key = key; }
	}
	/** MD算法 */
	public enum MDEnum {
		/** MD5 */
		MD5("MD5"),
		;
		private String key;
		public String getKey() { return key; }
		private MDEnum(String key) { this.key = key; }
	}
	/** SHA算法 */
	public enum SHAEnum {
		/** SHA-256 */
		SHA_256("SHA-256"),
		;
		private String key;
		public String getKey() { return key; }
		private SHAEnum(String key) { this.key = key; }
	}
	/** DES算法 */
	public enum DESEnum {
		/** DES对称加密 */
		DES("DES"),
		;
		private String key;
		public String getKey() { return key; }
		private DESEnum(String key) { this.key = key; }
	}
	
}
