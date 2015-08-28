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

import com.pytsoft.cachelock.util.Constants;
import net.spy.memcached.internal.OperationFuture;

/**
 * The {@code MemcachedClient} class is the Memcached client implementation of {@code CacheClient} with usage of spymemcached library.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.connector.CacheClient
 * @since 1.0.0
 */
public class MemcachedClient implements CacheClient {

    protected net.spy.memcached.MemcachedClient client;

    public MemcachedClient(net.spy.memcached.MemcachedClient client) {
        this.client = client;
    }

    @Override
    public boolean setnx(String key, String value, int expSeconds) {
        // Use add method to implement set if not exists API.
        OperationFuture<Boolean> result = this.client.add(key, expSeconds, value);
        try {
            return result.get();
        } catch (Exception e) {
            LOG.error("Error occurs while performing setnx for key[%s] and value[%s], reason:[%s]", key, value, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hsetnx(String key, String field, String value, int expSeconds) {
        // Convert key and field into a new key to implement hset API
        return this.setnx(this.genHashKey(key, field), value, expSeconds);
    }

    @Override
    public String get(String key) {
        return this.client.get(key).toString();
    }

    @Override
    public String hget(String key, String field) {
        // Get value from a new key converted from key and field to implement hget API
        return this.get(this.genHashKey(key, field)).toString();
    }

    @Override
    public void del(String key) {
        this.client.delete(key);
    }

    @Override
    public void hdel(String key, String field) {
        // Convert key and field into a new key to implement hdel API
        this.del(this.genHashKey(key, field));
    }

    // Generate a new key depends on key, field, and default separator.
    private String genHashKey(String key, String field) {
        return key + Constants.DEFAULT_SEPARATOR + field;
    }
}
