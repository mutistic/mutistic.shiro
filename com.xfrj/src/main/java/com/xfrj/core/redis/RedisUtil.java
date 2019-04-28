package com.xfrj.core.redis;

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

	public static void setRedisTemplate(RedisTemplate redisTemplate) {
		if (null == redisTemplate) {
			throw new RedisCacheException("redis-init:redisTemplate初始化失败！");
		}

		RedisUtil.redisTemplate = redisTemplate;
		opsForValue = new RedisStringCache(redisTemplate().opsForValue());
		opsForHash = new RedisHashCache(redisTemplate().opsForHash());
		opsForHash.setOpsForValue(opsForValue);
	}

	public static RedisTemplate redisTemplate() {
		if (null == redisTemplate) {
			throw new RedisCacheException("redis-init:redisTemplate未初始化！");
		}
		return redisTemplate;
	}
}
