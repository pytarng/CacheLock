package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.RedisClient;

/**
 * The {@code RedisLock} class is the Redis child class of {@code CacheLock} with usage of {@code RedisClient}.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.beans.CacheLock
 * @since JDK 1.6
 */
public class RedisLock extends CacheLock {

	public RedisLock(String key, RedisClient client) {
		super(key, client);
	}

	public RedisLock(String key, String field, RedisClient client) {
		super(key, field, client);
	}
}
