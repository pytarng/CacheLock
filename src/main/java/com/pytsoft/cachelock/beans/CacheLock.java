package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.Constants;

public class CacheLock {
	protected String key;
	protected String field;

	protected boolean hashLock = false;
	protected boolean locked = false;

	protected CacheClient client;

	public CacheLock(String key, CacheClient client) {
		this.key = Constants.CACHE_KEY_HEAD_LOCKER + key;
		this.client = client;
	}

	public CacheLock(String key, String field, CacheClient client) {
		this(key, client);
		this.field = Constants.CACHE_KEY_HEAD_LOCKER + field;
		this.hashLock = true;
	}

	public String getKey() {
		return key;
	}

	public String getField() {
		return field;
	}

	public boolean isHashLock() {
		return hashLock;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public CacheClient getClient() {
		return client;
	}
}
