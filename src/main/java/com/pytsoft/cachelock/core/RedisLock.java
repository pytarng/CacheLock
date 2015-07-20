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

package com.pytsoft.cachelock.core;

import com.pytsoft.cachelock.config.Configuration;
import com.pytsoft.cachelock.connector.RedisClient;

/**
 * The {@code RedisLock} class is the Redis child class of {@code CacheLock} with usage of {@code RedisClient}.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.core.CacheLock
 * @since JDK 1.6
 */
public class RedisLock extends CacheLock {

    public RedisLock(String key, RedisClient client) {
        super(key, client);
    }

    public RedisLock(String key, RedisClient client, Configuration config) {
        super(key, client, config);
    }

    public RedisLock(String key, String field, RedisClient client) {
        super(key, field, client);
    }

    public RedisLock(String key, String field, RedisClient client, Configuration config) {
        super(key, field, client, config);
    }
}
