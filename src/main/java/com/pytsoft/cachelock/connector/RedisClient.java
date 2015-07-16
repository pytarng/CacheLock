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

package com.pytsoft.cachelock.connector;

import com.pytsoft.cachelock.util.KeyUtils;
import redis.clients.jedis.Jedis;

/**
 * The {@code RedisClient} class is the Redis client implementation of {@code CacheClient} with usage of Jedis library.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.connector.CacheClient
 * @since JDK 1.6
 */
public class RedisClient implements CacheClient {

	protected Jedis jedis;

	public RedisClient(Jedis jedis) {
		this.jedis = jedis;
	}

	@Override
	public boolean setnx(String key, String value, int expSeconds) {
		if (this.jedis.setnx(key, value) == 1) {

			// Add key-value pair expiration settings to redis server.
			this.jedis.expire(key, expSeconds);
			return true;
		}
		return false;
	}

	@Override
	public boolean hsetnx(String key, String field, String value, int expSeconds) {
		if (this.jedis.hsetnx(key, field, value) == 1) {
			return true;
		} else {
			// Expiration setting is not supported in hsetnx case, need further operation.
			// Parse expiration time from stored value in key-value pair.
			String lockValue = this.hget(key, field);
			long expTime = KeyUtils.parseTime(lockValue);

			// Check whether previous set value is already expired.
			if (System.currentTimeMillis() - expTime > expSeconds * 1000) {

				// If already expired, delete field and hset again.
				this.hdel(key, field);
				return this.jedis.hsetnx(key, field, value) == 1;
			}
		}
		return false;
	}

	@Override
	public String get(String key) {
		return this.jedis.get(key);
	}

	@Override
	public String hget(String key, String field) {
		return this.jedis.hget(key, field);
	}

	@Override
	public void del(String key) {
		this.jedis.del(key);
	}

	@Override
	public void hdel(String key, String field) {
		this.jedis.hdel(key, field);
	}
}
