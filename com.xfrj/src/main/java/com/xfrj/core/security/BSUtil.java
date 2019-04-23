package com.xfrj.core.security;

public class BSUtil {

	private final static String CHARSET = "UTF-8";
	
	public static byte[] toBytes(String str) {
		try {
			return str.getBytes(CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("转码失败", e);
		}

	}

	public static String toString(byte[] bytes) {
		return new String(bytes);
	}

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String toHexString(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int temp = 0;
		int index = 0;
		for (byte b : bytes) {
			temp = (b < 0 ? 256 : 0) + b;
			buf[index++] = HEX_CHAR[temp / 16];
			buf[index++] = HEX_CHAR[temp % 16];
		}

		return new String(buf);
	}
	
	public static void main(String[] args) {
		System.out.println(toHexString(new byte[] {-81, 0, 105, 7, -32, 26, -49, 88}));
	}
}
