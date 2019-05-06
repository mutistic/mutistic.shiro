package com.mutistic.shiro.core.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;

public class RedisHashCache {

	private HashOperations opsForHash;

	public RedisHashCache(HashOperations opsForHash) {
		RedisUtil.notNullMsg(opsForHash, "redis-init-opearion: opsForHash cannot be null");
		this.opsForHash = opsForHash;
	}
	public HashOperations opsForHash() {
		return opsForHash;
	}

	public List<Object> multiGet(String key, Set<String> sufixFileds) {
		return opsForHash.multiGet(key, sufixFileds);
	}

	public Set<String> keys(String key) {
		return opsForHash.keys(key);
	}

	public Long delete(String key, String fileds) {
		return opsForHash.delete(key, fileds);
	}

	public Long delete(String key, Collection<String> fileds) {
		return opsForHash.delete(key, fileds.toArray());
	}

	public List<Object> values(String key) {
		return opsForHash.values(key);
	}

	public Map<String, Object> entries(String key) {
		return opsForHash.entries(key);
	}

	public boolean hasKey(String key, String filed) {
		return opsForHash.hasKey(key, filed);
	}

	public Object get(String key, String filed) {
		return opsForHash.get(key, filed);
	}

	public void putIfAbsent(String key, String filed, Object value) {
		RedisUtil.notTrue(opsForHash.putIfAbsent(key, filed, value),
				"redis-hash-ops：[key=" + key + ",filed=" + filed + "]已存在，putIfAbsent设置失败！");
	}

	public void put(String key, String filed, Object value) {
		put(key, fv2Map(filed, value));
	}

	public void put(String key, Map<String, Object> filedValueMap) {
		opsForHash.putAll(key, filedValueMap);
	}

	@SuppressWarnings("serial")
	public Map<String, Object> fv2Map(String filed, Object value) {
		return new HashMap<String, Object>() {
			{
				put(filed, value);
			}
		};
	}

}
