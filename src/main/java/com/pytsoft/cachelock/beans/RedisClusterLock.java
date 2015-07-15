package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.RedisClusterClient;

/**
 * The {@code RedisLock} class is the Redis-cluster child class of {@code CacheLock} with usage of {@code RedisClusterClient}.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.beans.CacheLock
 * @since JDK 1.6
 */
public class RedisClusterLock extends CacheLock {

	public RedisClusterLock(String key, RedisClusterClient client) {
		super(key, client);
	}

	public RedisClusterLock(String key, String field, RedisClusterClient client) {
		super(key, field, client);
	}
}
