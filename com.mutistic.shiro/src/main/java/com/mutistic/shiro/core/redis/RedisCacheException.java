package com.mutistic.shiro.core.redis;

@SuppressWarnings("serial")
public class RedisCacheException extends RuntimeException {

	public RedisCacheException() {
		super();
	}

	public RedisCacheException(String message) {
		super(message);
	}

	public RedisCacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisCacheException(Throwable cause) {
		super(cause);
	}
}
