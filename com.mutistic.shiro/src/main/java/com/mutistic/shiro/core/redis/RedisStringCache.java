package com.mutistic.shiro.core.redis;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;

public class RedisStringCache {

	private ValueOperations opsForValue;

	public RedisStringCache(ValueOperations opsForValue) {
		RedisUtil.notNullMsg(opsForValue, "redis-init-opearion: opsForValue cannot be null");
		this.opsForValue = opsForValue;
	}

	public Object get(String key) {
		return opsForValue.get(key);
	}
	
	public List<Object> get(Collection<String> keys) {
		return opsForValue.multiGet(keys);
	}

	public Long increment(String key) {
		return opsForValue.increment(key);
	}
	
	public Boolean delete(String key) {
		return opsForValue.getOperations().delete(key);
	}
	
	public Long delete(Collection<String> key) {
		return opsForValue.getOperations().delete(key);
	}
	
	public Object set(String key, Object value) {
		return set(key, value, null, null);
	}

	public Object set(String key, Object value, Long expSeconds) {
		return set(key, value, expSeconds, null);
	}

	public Object set(String key, Object value, Long expSeconds, Boolean isAbsent) {
		if (key == null || value == null) {
			return null;
		}
		boolean result = true;
		if (null == expSeconds) {
			if (null == isAbsent) {
				opsForValue.set(key, value);
			}
			else if (isAbsent) { // 当key不存在是设置值
				result = opsForValue.setIfAbsent(key, value);
			}
			else { // 当key不存在是设置值
				result = opsForValue.setIfPresent(key, value);
			}
		} else { // 包含过期时间
			if (null == isAbsent) {
				opsForValue.set(key, value, expSeconds, TimeUnit.SECONDS);
			} else if (isAbsent) {
				result = opsForValue.setIfAbsent(key, value, expSeconds, TimeUnit.SECONDS);
			} else {
				result = opsForValue.setIfPresent(key, value, expSeconds, TimeUnit.SECONDS);
			}
		}

		if (!result) {
			String msg = "";
			if (null != isAbsent && isAbsent) {
				msg = "[key=" + key + "]已存在！setIfAbsent设置失败！";
			}
			if (null != isAbsent && !isAbsent) {
				msg = "[key=" + key + "]不存在！setIfPresent设置失败！";
			}
			throw new RedisCacheException("redis-string-ops：" + msg);
		}
		return value;
	}

}
