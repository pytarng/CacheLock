package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.MemcachedClient;

/**
 * The {@code MemcachedLock} class is the Memcached child class of {@code CacheLock} with usage of {@code MemcachedClient}.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.beans.CacheLock
 * @since JDK 1.6
 */
public class MemcachedLock extends CacheLock {

	public MemcachedLock(String key, MemcachedClient client) {
		super(key, client);
	}

	public MemcachedLock(String key, String field, MemcachedClient client) {
		super(key, field, client);
	}
}
