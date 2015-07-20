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
import com.pytsoft.cachelock.connector.CacheClient;

/**
 * The {@code OtherCacheLock} class is the child class of {@code CacheLock} for all customized client implementation.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.core.CacheLock
 * @since JDK 1.6
 */
public class OtherCacheLock extends CacheLock {
    public OtherCacheLock(String key, CacheClient client) {
        super(key, client);
    }

    public OtherCacheLock(String key, CacheClient client, Configuration config) {
        super(key, client, config);
    }

    public OtherCacheLock(String key, String field, CacheClient client) {
        super(key, field, client);
    }

    public OtherCacheLock(String key, String field, CacheClient client, Configuration config) {
        super(key, field, client, config);
    }
}
