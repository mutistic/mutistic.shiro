package com.mutistic.shiro.login.shiro.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * shiro cache使用redis缓存实现session的管理
 * @param <K> String:sessionId
 * @param <V> org.apache.shiro.session.mgt.SimpleSession：session实例
 */
public class ShiroRedisCache<K, V> implements Cache<K, V> {
	
	private RedisTemplate redisTemplate;
	private String cacheName;


	public ShiroRedisCache(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ShiroRedisCache(RedisTemplate redisTemplate, String cacheName) {
		this.redisTemplate = redisTemplate;
		this.cacheName = cacheName;
	}

	@Override
	public V get(K k) throws CacheException {
		if (k == null) {
			return null;
		}
		
		return (V) redisTemplate.opsForValue().get(k);

	}

	@Override
	public V put(K k, V v) throws CacheException {
		if (k == null || v == null) {
			return null;
		}
		
		System.out.println(JSONObject.toJSON(v));
		redisTemplate.opsForValue().set(k, v);
		return v;
	}

	@Override
	public V remove(K k) throws CacheException {
		if (k == null) {
			return null;
		}
		V v = (V) redisTemplate.opsForValue().get(k);
		redisTemplate.delete(k);
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
		return (Set<K>) redisTemplate.keys("*");
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

}
