package com.xfrj.core.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	
	@Bean
	public RedisTemplate<String, Serializable> serializableRedisTemplate() {
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
		
		// String 序列化
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		// JSON序列化
		GenericFastJsonRedisSerializer valueSerializer = new GenericFastJsonRedisSerializer();
		redisTemplate.setEnableTransactionSupport(false);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(keySerializer);
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashKeySerializer(keySerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	@Autowired
	private StringRedisTemplate  stringRedisTemplate;
	
	@Bean
	public RedisCache serializableRedisCache() {
//		RedisUtil.setRedisCache(new RedisCache(serializableRedisTemplate()));
		RedisUtil.setRedisCache(new RedisCache(stringRedisTemplate));
		return RedisUtil.getRedisCache();
	}

}
