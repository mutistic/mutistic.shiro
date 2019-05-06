package com.mutistic.shiro.core.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.mutistic.shiro.core.security.ArithmeticContant.DESEnum;

/**
 * DES算法
 */
public class DESCoder {

	/**
	 * 1、jdk8 javax.crypto api
	 * https://blog.fondme.cn/apidoc/jdk-1.8-google/javax/crypto/package-summary.html
	 */

	/** 加密/解密算法/工作模式 /填充方式 */
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5PADDING";
	
	/**
	 * DES加密
	 * 
	 * @param data
	 * @param encryptKey
	 * @return
	 */
	public static byte[] encrypt(String data, byte[] encryptKey) {
		return encrypt(BSUtil.toBytes(data), encryptKey);
	}

	/**
	 * DES加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		try {
			// 还原密钥
			Key k = toKey(key);
			// 实例化
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, k);
			// 执行操作
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(DESEnum.DES.getKey() +"加密失败", e);
		}
	}

	/**
	 * DES解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		try {
			// 还原密钥
			Key k = toKey(key);
			// 实例化
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, k);
			// 执行操作
			return cipher.doFinal(data);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(DESEnum.DES.getKey() +"解密失败", e);
		}
	}

	/**
	 * 转换密钥
	 * 
	 * @param key
	 *            二进制密钥
	 * @return Key 密钥
	 */
	private static Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		// 实例化DES密钥材料
		DESKeySpec dks = new DESKeySpec(key);
		// 实例化秘密密钥工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DESEnum.DES.getKey());
		// 生成秘密密钥
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}

}
