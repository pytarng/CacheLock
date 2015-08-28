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

package com.pytsoft.cachelock;

import com.pytsoft.cachelock.exception.LockFailedException;
import com.pytsoft.cachelock.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Test cases class for {@code LockSmith}.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.LockSmith
 * @since 1.0.0
 */
public abstract class LockSmithTest {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected LockSmith locker;

    protected String testTargetKey = "TEST_KEY_MEMCACHED_SPYMEMCACHED" + Constants.DEFAULT_SEPARATOR + UUID.randomUUID().toString();
    protected String cacheServerHost = "192.168.56.101";
    protected int redisServerPort = 6379;
    protected int memcachedServerPort = 11211;

    public void init() throws IOException {
        this.locker = new LockSmith();
    }

    public abstract void lock_then_unlock() throws LockFailedException;
}
