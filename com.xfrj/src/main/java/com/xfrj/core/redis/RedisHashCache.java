package com.xfrj.core.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;

public class RedisHashCache {

	private HashOperations opsForHash;
	private RedisStringCache opsForValue;
	private final static String EXP_HASH = "exp:hash:";
	private final static String KEY_FILED = ":";

	public void setOpsForValue(RedisStringCache opsForValue) {
		this.opsForValue = opsForValue;
	}

	public RedisHashCache(HashOperations opsForHash) {
		if (opsForHash == null) {
			throw new RedisCacheException("redis-init-opearion:注入HashOperations出现异常！");
		}

		this.opsForHash = opsForHash;
	}

	public Set<String> keys(String key) {
		if (prefixTimeout(key, null)) {
			return null;
		}

		return opsForHash.keys(key);
	}

	public List<Object> values(String key) {
		if (prefixTimeout(key, null)) {
			return null;
		}

		return opsForHash.values(key);
	}

	public Map<String, Object> entries(String key) {
		if (prefixTimeout(key, null)) {
			return null;
		}

		return opsForHash.entries(key);
	}

	public boolean hasKey(String key, String filed) {
		if (prefixTimeout(key, filed)) {
			return false;
		}

		return opsForHash.hasKey(key, filed);
	}

	public Object get(String key, String filed) {
		return opsForHash.get(key, filed);
	}

	public void putIfAbsent(String key, String filed, Object value) {
		boolean result = opsForHash.putIfAbsent(key, filed, value);
		if (!result) {
			throw new RedisCacheException("redis-hash-ops：[key=" + key + ",filed=" + filed + "]已存在，putIfAbsent设置失败！");
		}
	}

	public void put(String key, String filed, Object value) {
		put(key, toFiledValueMap(filed, value));
	}

	public void put(String key, Map<String, Object> filedValueMap) {
		opsForHash.putAll(key, filedValueMap);
	}

	public void put(String key, String filed, Object value, Long expSeconds) {
		put(key, filed, value);
		if (expSeconds == null || expSeconds.longValue() < 0l) {
			return;
		}

		String expKey = EXP_HASH + key + KEY_FILED + filed;
		opsForValue.set(expKey, getExpTime(expSeconds));
	}

	/**
	 * 针对hash设置过期时间的隐患：
	 * 
	 * @param key
	 * @param filedValueMap
	 * @param expSeconds
	 * @param toKey
	 */
	public void put(String key, Map<String, Object> filedValueMap, Long expSeconds, boolean toKey) {
		prefixTimeoutMap(key, toKey ? null : filedValueMap);

		put(key, filedValueMap);
		if (expSeconds == null || expSeconds.longValue() < 0l) {
			return;
		}

		Long expTime = getExpTime(expSeconds);
		if(toKey) {
			opsForValue.set(EXP_HASH + key, expTime);
		} 
		else {
			String expKey;
			for (String filed : filedValueMap.keySet()) {
				expKey = EXP_HASH + key + KEY_FILED + filed;
				opsForValue.set(expKey, expTime);
			}
		}
	}

	public void putByKey(String key, Map<String, Object> filedValueMap, Long expSeconds) {
		put(key, filedValueMap);
		if (expSeconds == null || expSeconds.longValue() < 0l) {
			return;
		}

		opsForValue.set(EXP_HASH + key, getExpTime(expSeconds));
	}

	private boolean prefixTimeout(String key, String filed) {
		String expKey = EXP_HASH + (filed == null || filed.length() == 0 ? EXP_HASH + key : key + KEY_FILED + filed);
		String expTime = (String) opsForValue.get(expKey);
		if (expTime != null && expTime.length() > 0
				&& Long.valueOf(expTime).compareTo(System.currentTimeMillis()) < 0) {
			opsForHash.delete(key, filed);
			opsForValue.delete(expKey);
			return true; // 已过期
		}

		return false; // 未过期或未设置过期时间
	}

	private boolean prefixTimeoutMap(String key, Map<String, Object> filedValueMap) {
		boolean notExistKey = prefixTimeout(key, null);
		if (notExistKey) {
			return true;
		}

		Set<String> filedSet = new HashSet<String>();
		String expKey, expTime;
		for (String filed : filedValueMap.keySet()) {
			expKey = EXP_HASH + (filed == null || filed.length() == 0 ? key : key + KEY_FILED + filed);
			expTime = (String) opsForValue.get(expKey);
			if (expTime != null && expTime.length() > 0
					&& Long.valueOf(expTime).compareTo(System.currentTimeMillis()) < 0) {
				filedSet.add(expKey);
			}
		}

		if (filedSet.size() > 0) {
			opsForHash.delete(key, filedSet.toArray());
			opsForValue.delete(filedSet);
			return true; // 已过期
		}

		return false; // 未过期或未设置过期时间
	}

	private Long getExpTime(Long expSeconds) {
		return System.currentTimeMillis() + expSeconds * 1000;
	}

	@SuppressWarnings("serial")
	private Map<String, Object> toFiledValueMap(String filed, Object value) {
		return new HashMap<String, Object>() {
			{
				put(filed, value);
			}
		};
	}
}
