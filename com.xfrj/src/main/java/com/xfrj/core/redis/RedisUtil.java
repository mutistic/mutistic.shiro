package com.xfrj.core.redis;

import com.alibaba.fastjson.JSONObject;

public class RedisUtil {
	
	private static RedisCache redisCache;
	public static RedisCache getRedisCache() {
		return redisCache;
	}
	public static void setRedisCache(RedisCache redisCache) {
		RedisUtil.redisCache = redisCache;
	}

	public static <T> T get(Object key, Class<T> cls) {
		try {
			Object obj = redisCache.get(key);
			return isJsonObject(obj) ? JSONObject.toJavaObject((JSONObject) obj, cls) : (T) obj;
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	public static Object get(Object key) {
		try {
			return redisCache.get(key);
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	public static Object set(Object key, Object value) {
		return set(key, value, null, null);
	}
	
	public static Object set(Object key, Object value, Long expSeconds) {
		return set(key, value, expSeconds, null);
	}
	
	public static Object set(Object key, Object value, Long expSeconds, Boolean isAbsent) {
		try {
			return redisCache.set(key, value, expSeconds, isAbsent);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RedisCacheException(e);
		}
	}
	
	private static boolean isJsonObject(Object obj){
		return obj != null && obj instanceof JSONObject;
	}

}
