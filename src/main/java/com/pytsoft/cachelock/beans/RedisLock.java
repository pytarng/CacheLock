package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.RedisClient;

public class RedisLock extends CacheLock {

	public RedisLock(String key, RedisClient client) {
		super(key, client);
	}

	public RedisLock(String key, String field, RedisClient client) {
		super(key, field, client);
	}
}
