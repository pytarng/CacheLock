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
