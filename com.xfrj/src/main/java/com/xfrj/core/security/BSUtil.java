package com.xfrj.core.security;

/**
 * Byte String 工具类
 */
public class BSUtil {

	/** UTF-8编码格式 */
	private final static String CHARSET = "UTF-8";
	
	/**
	 * 转换成byte数组(charset=UTF-8)
	 * @param str
	 * @return
	 */
	public static byte[] toBytes(String str) {
		try {
			return str.getBytes(CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("转码失败", e);
		}
	}

	/**
	 * 转换成字符串
	 * @param bytes
	 * @return
	 */
	public static String toString(byte[] bytes) {
		return new String(bytes);
	}
	
	private final static char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	/**
	 * 转换为十六进制字符串
	 * @param bytes
	 * @return
	 */
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
	/**
	 * 返回 byte数组字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesValue(byte[] bytes) {
		StringBuffer sb = new StringBuffer("{");
		for (byte b : bytes) {
			sb.append(b +", ");
		}
		return sb.substring(0, sb.length()-2) + "}";
	}
	
	public static void main(String[] args) {
		System.out.println(toHexString(new byte[] {-81, 0, 105, 7, -32, 26, -49, 88}).length());
	}
}
