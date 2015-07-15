package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.MemcachedClient;

public class MemcachedLock extends CacheLock {

	public MemcachedLock(String key, MemcachedClient client) {
		super(key, client);
	}

	public MemcachedLock(String key, String field, MemcachedClient client) {
		super(key, field, client);
	}
}
