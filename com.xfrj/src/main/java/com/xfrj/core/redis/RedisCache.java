package com.xfrj.core.redis;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

/**
 * redis 缓存
 */
public class RedisCache {
	//https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/RedisTemplate.html
	//https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ValueOperations.html
	// https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html
	// 
	/** spring redis templeate */
	private RedisTemplate redisTemplate;
	/** true：redis String数据类型的序列化方式是GenericFastJsonRedisSerializer */
	private boolean isJson;
	
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public RedisCache(RedisTemplate redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.isJson = redisTemplate.getValueSerializer() instanceof GenericFastJsonRedisSerializer;
	}

	public Object get(Object key) {
		Object value = redisTemplate.opsForValue().get(key);
		return isJson ? value : JSONObject.parseObject((String)value);
	}

	public Object set(Object key, Object value) {
		return set(key, value, null, null);
	}
	
	public Object set(Object key, Object value, Long expSeconds) {
		return set(key, value, expSeconds, null);
	}

	public Object set(Object key, Object value, Long expSeconds, Boolean isAbsent) {
		if (key == null || value == null) {
			return null;
		}
		
		if (!isJson) {
			key = toJsonKey(key);
			value = toJsonValue(value);
		}
		
		if(null == expSeconds) {
			if(null == isAbsent){
				redisTemplate.opsForValue().set(key, value);
			} 
			// 当key不存在是设置值
			else if(isAbsent){
				redisTemplate.opsForValue().setIfAbsent(key, value);
			} 
			else {
				redisTemplate.opsForValue().setIfPresent(key, value);
			}
		} 
		// 过期时间
		else {
			// 
			if(null == isAbsent){
				redisTemplate.opsForValue().set(key, value, expSeconds, TimeUnit.SECONDS);
			} 
			// 当key不存在是设置值
			else if(isAbsent){
				redisTemplate.opsForValue().setIfAbsent(key, value, expSeconds, TimeUnit.SECONDS);
			} 
			else {
				redisTemplate.opsForValue().setIfPresent(key, value, expSeconds, TimeUnit.SECONDS);
			}
		}
		return value;
	}
	
	private Object toJsonKey(Object key) {
		if(key instanceof String){
			return key;
		}
		
		String keyStr = JSONObject.toJSONString(key);
		return keyStr.substring(1, keyStr.length()-1);
	}
	
	private Object toJsonValue(Object value) {
		if(value instanceof String){
			return value;
		}
		return JSONObject.toJSONString(value);
	}

}
