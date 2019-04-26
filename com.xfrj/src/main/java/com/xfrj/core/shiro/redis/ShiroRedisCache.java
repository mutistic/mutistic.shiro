package com.xfrj.core.shiro.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

@SuppressWarnings({"unchecked" })
public class ShiroRedisCache<K, V> implements Cache<K, V> {
	
	private RedisTemplate<Object, Object> redisTemplate;
	private String prefix = "shiro-session:";


	public ShiroRedisCache(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ShiroRedisCache(RedisTemplate<Object, Object> redisTemplate, String prefix) {
		this(redisTemplate);
		this.prefix += prefix;
	}

	@Override
	public V get(K k) throws CacheException {
		if (k == null) {
			return null;
		}
		return (V) redisTemplate.opsForValue().get(prefix(k));

	}

	@Override
	public V put(K k, V v) throws CacheException {
		if (k == null || v == null) {
			return null;
		}

		redisTemplate.opsForValue().set(prefix(k), v);
		return v;
	}

	@Override
	public V remove(K k) throws CacheException {
		if (k == null) {
			return null;
		}
		V v = (V) redisTemplate.opsForValue().get(prefix(k));
		redisTemplate.delete(prefix(k));
		return v;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.getConnectionFactory().getConnection().flushDb();

	}

	@Override
	public int size() {
		return redisTemplate.getConnectionFactory().getConnection().dbSize().intValue();
	}

	@Override
	public Set<K> keys() {
		return (Set<K>) redisTemplate.keys(this.prefix + "*");
	}

	@Override
	public Collection<V> values() {
		Set<K> keys = keys();
		List<V> values = new ArrayList<>(keys.size());
		for (K k : keys) {
			values.add(get(k));
		}
		return values;
	}

	private String prefix(K key) {
		return this.prefix + key;
	}

}
