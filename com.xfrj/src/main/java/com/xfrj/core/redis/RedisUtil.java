package com.xfrj.core.redis;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {
	private RedisUtil() { }

	/**
	 * RedisCache.getRedisTemplate()：
	 * KeySerializer/HashKeySerializer：org.springframework.data.redis.serializer.StringRedisSerializer
	 * ValueSerializer/HashValueSerializer：com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer
	 */
	/** spring redis templeate */
	private static RedisTemplate redisTemplate;
	public static RedisStringCache opsForValue;
	public static RedisHashCache opsForHash;
	public static RedisHashExpCache opsForHashExp;

	public static void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisUtil.notNullMsg(redisTemplate,  "redis-init: redisTemplate cannot be null");

		RedisUtil.redisTemplate = redisTemplate;
		opsForValue = new RedisStringCache(redisTemplate().opsForValue());
		opsForHash = new RedisHashCache(redisTemplate().opsForHash());
		opsForHashExp = new RedisHashExpCache(opsForHash, opsForValue);
	}

	public static RedisTemplate redisTemplate() {
		RedisUtil.notNullMsg(redisTemplate,  "redis-init: redisTemplate not init");
		return redisTemplate;
	}
	
	public static boolean isEmpty(String obj) {
		return obj == null || obj.trim().length() == 0;
	}
	public static boolean isEmpty(Collection<?> obj) {
		return obj == null || obj.isEmpty();
	}
	public static boolean isEmpty(Map<?, ?> obj) {
		return obj == null || obj.isEmpty();
	}
	public static void exception(String msg) {
		throw new RedisCacheException(msg);
	}
	public static void notNull(Object obj, String msg) {
		notNullMsg(obj, msg == null ? "错误" : msg + "不能为空！");
	}
	public static void notNullMsg(Object obj, String msg) {
		msg = msg == null ? "错误" : msg;
		
		if(obj == null) {
			throw new RedisCacheException(msg);
		}
		if(obj instanceof String && ((String) obj).trim().length() == 0) {
			throw new RedisCacheException(msg);
		}
		if(obj instanceof Collection && ((Collection) obj).isEmpty()) {
			throw new RedisCacheException(msg);
		}
		if(obj instanceof Map && ((Map) obj).isEmpty()) {
			throw new RedisCacheException(msg);
		}
		if(obj.getClass().isArray() && ((Object[])obj).length == 0) {
			throw new RedisCacheException(msg);
		}
	}
	public static void notTrue(Boolean obj, String msg) {
		if(!obj) {
			throw new RedisCacheException(msg);
		}
	}
	public static void notExpTime(Long expTime) {
		if(expTime == null || expTime.longValue() <= 0) {
			throw new RedisCacheException("过期时间不能为空！");
		}
	}
}
