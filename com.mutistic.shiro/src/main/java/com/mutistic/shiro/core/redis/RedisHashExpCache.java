package com.mutistic.shiro.core.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hash扩展过期时间操作（针对Filed）
 */
public class RedisHashExpCache {

	public final static String HASH_KEY_PREFIX = "hash:data:";
	private final static String EXP_PREFIX = "hash:exp:";
	private final static String KF_JOIN = ":";
	private static final String NO_EXP_FILED = "noExpFiled";
	private static final String EXP_FILED = "expFiled";

	private RedisStringCache stringCache;
	private RedisHashCache hashCache;

	public RedisHashExpCache(RedisHashCache hashCache, RedisStringCache stringCache) {
		RedisUtil.notNullMsg(hashCache, "redis-init-opearion: hashCache cannot be null");
		RedisUtil.notNullMsg(stringCache, "redis-init-opearion: stringCache cannot be null");
		this.hashCache = hashCache;
		this.stringCache = stringCache;
	}

	public Set<String> keys(String key) {
		key = prefixKey(key);
		return (Set<String>) deleteExpData(key, hashCache.keys(key)).get(NO_EXP_FILED);
	}

	public List<Object> values(String key) {
		key = prefixKey(key);

		Set<String> sufixFileds = this.keys(key);
		if (RedisUtil.isEmpty(sufixFileds)) {
			return new ArrayList<Object>();
		}

		return hashCache.multiGet(key, sufixFileds);
	}

	public Map<String, Object> entries(String key) {
		key = prefixKey(key);

		Map<String, Object> entriesMap = hashCache.entries(key);

		Map<String, Set<String>> suffixMap = deleteExpData(key, entriesMap.keySet());
		for (String expFiled : suffixMap.get(EXP_FILED)) {
			entriesMap.remove(expFiled);
		}

		return entriesMap;
	}

	public boolean hasKey(String key, String filed) {
		key = prefixKey(key);

		if (deleteExpData(key, filed)) {
			return false;
		}
		return hashCache.hasKey(key, filed);
	}

	public Object get(String key, String filed) {
		key = prefixKey(key);

		if (deleteExpData(key, filed)) {
			return null;
		}
		return hashCache.get(key, filed);
	}

	public void put(String key, String filed, Object value, Long expSeconds) {
		RedisUtil.notExpTime(expSeconds);
		key = prefixKey(key);

		hashCache.put(key, filed, value);
		insertExpData(key, filed, expSeconds);
	}

	public void put(String key, Map<String, Object> filedValueMap, Long expSeconds) {
		RedisUtil.notExpTime(expSeconds);
		key = prefixKey(key);

		hashCache.put(key, filedValueMap);
		insertExpData(key, filedValueMap, expSeconds);
	}

	public void putIfAbsent(String key, String filed, Object value, Long expSeconds) {
		RedisUtil.notExpTime(expSeconds);
		key = prefixKey(key);

		hashCache.putIfAbsent(key, filed, value);
		insertExpData(key, filed, expSeconds);
	}
	
	public Set<String> expKeys(String key) {
		return hashCache.opsForHash().getOperations().keys(getExpKey(key, "*"));
	}
	public Map<String, String> expEntries(String key) {
		Map<String, String> entriesMap = new HashMap<String, String>();
		Set<String> keySet = expKeys(key);
		if(RedisUtil.isEmpty(keySet)) {
			return entriesMap;
		}
		for (String expKey : keySet) {
			entriesMap.put(expKey, (String)stringCache.get(expKey));
		}
		return entriesMap;
	}
	public void delteExpKey(String key) {
		Map<String, String> expEntriesMap = expEntries(key);
		if(RedisUtil.isEmpty(expEntriesMap)) {
			return;
		}
		
		Set<String> expKeySet = new HashSet<String>();
		Set<String> filedSet = new HashSet<String>();
		
		String expHashKey = prefixKey(key);
		for (String expKey : expEntriesMap.keySet()) {
			if(isExp(expEntriesMap.get(expKey))) {
				expKeySet.add(expKey);
				filedSet.add(expKey.replace(expHashKey, ""));
			}
		}
		
		
		
		hashCache.delete(expHashKey, filedSet);
		stringCache.delete(expKeySet);
	}
	

	private String prefixKey(String key) {
		RedisUtil.notNull(key, "hash key");
		return key.contains(HASH_KEY_PREFIX) ? key : HASH_KEY_PREFIX + key;
	}

	private void insertExpData(String key, String filed, Long expSeconds) {
		stringCache.set(getExpKey(key, filed), getExpValue(expSeconds));
	}
	private void insertExpData(String key, Map<String, Object> filedValueMap, Long expSeconds) {
		String expValue = getExpValue(expSeconds);
		for (String filed : filedValueMap.keySet()) {
			stringCache.set(getExpKey(key, filed), expValue);
		}
	}
	
	private String getExpKey(String key, String filed) {
		return EXP_PREFIX + key.replace(HASH_KEY_PREFIX, "") + KF_JOIN + (filed == null ? "" : filed);
	}
	private String getExpValue(Long expSeconds) {
		return (System.currentTimeMillis() + expSeconds * 1000) + "";
	}

	private boolean deleteExpData(String key, String filed) {
		String expKey = getExpKey(key, filed);
		if (isExp((String)stringCache.get(expKey))) {
			hashCache.delete(key, filed);
			stringCache.delete(expKey);
			return true; // 已过期
		}

		return false; // 未过期或未设置过期时间
	}

	private Map<String, Set<String>> deleteExpData(String key, Set<String> filedSet) {
		Map<String, Set<String>> resultMap = new HashMap<String, Set<String>>();
		if (RedisUtil.isEmpty(key) || RedisUtil.isEmpty(filedSet)) {
			resultMap.put(NO_EXP_FILED, filedSet);
			return resultMap;
		}

		String expKey;
		Map<String, String> expFiledMap = new HashMap<String, String>();
		for (String tempFiled : filedSet) {
			expKey = getExpKey(key, tempFiled);
			if (isExp((String)stringCache.get(expKey))) {
				expFiledMap.put(tempFiled, expKey);
			}
		}

		if (!expFiledMap.isEmpty()) {
			hashCache.delete(key, expFiledMap.keySet()); // exp:hash中删除过期数据
			stringCache.delete(expFiledMap.values()); // exp:second中删除过期数据
			filedSet.removeAll(expFiledMap.keySet());
		}

		resultMap.put(NO_EXP_FILED, filedSet); // 未过期filed
		resultMap.put(EXP_FILED, expFiledMap.keySet()); // 过期expKey
		return resultMap;
	}
	
	private boolean isExp(String expTime) {
		return expTime != null && expTime.length() > 0
				&& Long.valueOf(expTime).compareTo(System.currentTimeMillis()) <= 0;
	}

}
