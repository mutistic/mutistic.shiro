package com.mutistic.shiro.login.regiest;

import java.util.concurrent.ThreadLocalRandom;

import com.mutistic.shiro.core.redis.RedisUtil;

public class RegiestUtil {

	/** 验证码 key */
	public final static String REGIEST_PREFIX = "S:mutistic:regiest:";
	/**
	 * 验证码有效期 (秒) </br>
	 * 5分钟
	 */
	public final static Long REGIEST_CODE_EXP_TIME = 5 * 60l;
	/** 一天内验证码次数 */
	public final static Integer REGIEST_NUMBER = 5;
	/**
	 * 一天内验证码次数有效期(秒) </br>
	 * 24小时
	 */
	public final static Long REGIEST_NUMBER_EXP_TIME = 24 * 60 * 60l;

	public final static String RANDOM_DATA = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";

	public static String generateNumberCode(int length) {
		StringBuffer sb = new StringBuffer(6);
		for (int i = 0; i < length; i++) {
			sb.append(ThreadLocalRandom.current().nextInt(9));
		}
		return sb.toString();
	}

	public static String generateCode(int length) {
		StringBuffer sb = new StringBuffer(6);
		for (int i = 0; i < length; i++) {
			sb.append(RANDOM_DATA.charAt(ThreadLocalRandom.current().nextInt(RANDOM_DATA.length())));
		}
		return sb.toString();
	}

	public static String setMobileInfo(String mobile) {
		String code = generateNumberCode(6);
		setCodeInfo(mobile, code);
		setNumber(mobile);
		return code;
	}
	
	public static String getCodeInfo(String mobile) {
		return (String) RedisUtil.opsForValue.get(regiestKey(mobile));
	}
	
	public static void setCodeInfo(String mobile, String code) {
		RedisUtil.opsForValue.set(regiestKey(mobile), code, REGIEST_CODE_EXP_TIME);
	}
	
	public static void deleteCode(String mobile) {
		RedisUtil.opsForValue.delete(regiestKey(mobile));
		RedisUtil.opsForValue.delete(numberKey(mobile));
	}
	
	public static Integer getNumber(String mobile) {
		Integer num = (Integer) RedisUtil.opsForValue.get(numberKey(mobile));
		return num == null ? 0 : num;
	}
	public static Integer setNumber(String mobile) {
		Integer num = getNumber(mobile)+1;
		RedisUtil.opsForValue.set(numberKey(mobile), num, REGIEST_NUMBER_EXP_TIME);
		return num;
	}

	private static String regiestKey(String mobile) {
		return REGIEST_PREFIX + "CODE:" + mobile;
	}
	private static String numberKey(String mobile) {
		return REGIEST_PREFIX + "NUMBER:" + mobile;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(RegiestUtil.generateNumberCode(4));
		}
	}

}
