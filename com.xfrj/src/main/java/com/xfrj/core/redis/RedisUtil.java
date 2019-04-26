package com.xfrj.core.redis;

import org.apache.shiro.cache.CacheException;

import com.alibaba.fastjson.JSONObject;


public class RedisUtil {
	private static RedisCache redisCache;
	public static RedisCache getRedisCache() {
		return redisCache;
	}
	public static void setRedisCache(RedisCache redisCache) {
		RedisUtil.redisCache = redisCache;
	}

	public static <T> T get(Object key, Class<T> cls) throws CacheException {
		Object obj = redisCache.get(key);
		return (obj instanceof JSONObject) ? JSONObject.toJavaObject((JSONObject)obj, cls) : (T) obj;
	}
	
	public static Object get(Object key) throws CacheException {
		return redisCache.get(key);
	}
	
	public static Object set(Object key, Object value) throws CacheException {
		return redisCache.set(key, value);
	}
	
}
