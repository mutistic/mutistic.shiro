package com.xfrj.core.redis;

import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

public class RedisCache {
	private RedisTemplate redisTemplate;

	private boolean isNotJson;
	
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public RedisCache(RedisTemplate redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.isNotJson = redisTemplate.getValueSerializer() instanceof GenericFastJsonRedisSerializer;
	}

	public Object get(Object key) throws CacheException {
		Object value = redisTemplate.opsForValue().get(key);
		return isNotJson ? value : JSONObject.parseObject((String)value);
	}

	public Object set(Object key, Object value) throws CacheException {
		if (key == null || value == null) {
			return null;
		}
		
		if (isNotJson) {
			key = toJsonKey(key);
			value = toJsonValue(value);
		}
		
		redisTemplate.opsForValue().set(key, value);
		return value;
	}

	private Object toJsonKey(Object key) {
		String keyStr = JSONObject.toJSONString(key);
		return keyStr.substring(1, keyStr.length()-1);
	}
	
	private Object toJsonValue(Object value) {
		return JSONObject.toJSONString(value);
	}

}
