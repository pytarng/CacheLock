/*
 * Copyright 2015 Ben PY Tarng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.Constants;

/**
 * The {@code CacheLock} abstract class defines variables to represent for a lock target.
 * <p>
 * There are two kinds of constructor, with two arguments (key and client) and three arguments (key, field, and client)
 * separately, represents for key lock and hash field lock.
 * <p>
 * The lock class and cache client class should be in pairs, and this parent abstract class should not be used directly.
 * For example:
 * <blackquote><pre>
 *     CacheClient redisClient = new RedisClient(jedis);
 *     CacheLock cacheLock = new RedisLock("test_key", redisClient);
 *     try {
 *         locker.lock(cacheLock);
 *     }
 *     finally {
 *         locker.unlock(cacheLock);
 *     }
 * </pre></blackquote>
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.beans.RedisLock
 * @see com.pytsoft.cachelock.beans.RedisClusterLock
 * @see com.pytsoft.cachelock.beans.MemcachedLock
 * @since JDK 1.6
 */
public abstract class CacheLock {
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
