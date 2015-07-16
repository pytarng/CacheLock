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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code CacheClient} interface defines the required commands for cache client to implement.
 * <p>
 * <a href="http://redis.io/">Redis</a> (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib)
 * , Redis cluster (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib),
 * and <a href="http://memcached.org/">Memcached</a>
 * (with <a href="https://github.com/couchbase/spymemcached">spymemcached</a> lib) are implemented by default.
 * <p>
 * The required commands for the cache client implementation are as follows:
 * <ul>
 * <li> setnx
 * <li> hsetnx
 * <li> get
 * <li> hget
 * <li> del
 * <li> hdel
 * </ul>
 * <p>
 * Please reference the descriptions on each method for more information.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.connector.RedisClient
 * @see com.pytsoft.cachelock.connector.RedisClusterClient
 * @see com.pytsoft.cachelock.connector.MemcachedClient
 * @since JDK 1.6
 */
public interface CacheClient {

	Logger LOG = LoggerFactory.getLogger(CacheClient.class);

	/**
	 * Command SETNX: set key-value pair to cache server if key not exists.
	 *
	 * @param key
	 * 		Cache key
	 * @param value
	 * 		Value
	 * @param expSeconds
	 * 		Lifetime for a key-value pair to exist on cache server
	 *
	 * @return {@code True} if key not exists and key-value pair set successfully
	 */
	boolean setnx(String key, String value, int expSeconds);

	/**
	 * Command HSETNX: set field-value pair to hashed key on cache server if field not exists.
	 *
	 * @param key
	 * 		Cache key
	 * @param field
	 * 		Hashed field in cache key
	 * @param value
	 * 		Value
	 * @param expSeconds
	 * 		Lifetime for a field-value pair to exist on hashed cache key
	 *
	 * @return {@code True} if field not exists and field-value pair hashed set successfully
	 */
	boolean hsetnx(String key, String field, String value, int expSeconds);

	/**
	 * Command GET: get value from key-value pair from cache server.
	 *
	 * @param key
	 * 		Cache key
	 *
	 * @return Value stored in key-value pair
	 */
	String get(String key);

	/**
	 * Command HGET: get value from field-value pair from hashed cache key.
	 *
	 * @param key
	 * 		Cache key
	 * @param field
	 * 		Field in hashed cache key
	 *
	 * @return Value stored in field-value pair from hashed cache key
	 */
	String hget(String key, String field);

	/**
	 * Command DEL: delete key from cache server.
	 *
	 * @param key
	 * 		Cache key
	 */
	void del(String key);

	/**
	 * Command HDEL: delete field from hashed cache key.
	 *
	 * @param key
	 * 		Cache key
	 * @param field
	 * 		Field in hashed cache key
	 */
	void hdel(String key, String field);
}
