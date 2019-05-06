package com.mutistic.shiro.login.shiro.session;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * shiro cache 使用 redis
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {

	private RedisTemplate redisTemplate;

	public ShiroRedisCacheManager(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected Cache createCache(String cacheName) throws CacheException {
		return new ShiroRedisCache(redisTemplate, cacheName);
	}
}