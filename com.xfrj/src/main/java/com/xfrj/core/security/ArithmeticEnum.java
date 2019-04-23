package com.xfrj.core.security;

/**
 * 算法枚举类
 */
public enum ArithmeticEnum {

	/** BASE64 */
	BASE64("Base64"),
	/** MD5 */
	MD5("MD5"),
	/** SHA-256 */
	SHA_256("SHA-256"),
	;

	private String key;

	private ArithmeticEnum(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	
}
