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

import com.pytsoft.cachelock.core.RedisLock;
import com.pytsoft.cachelock.connector.RedisClient;
import com.pytsoft.cachelock.exception.LockFailedException;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * Test cases class for {@code LockSmith} for redis server.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.LockSmith
 * @since JDK 1.6
 */
public class LockSmithTest_Redis_Jedis extends LockSmithTest {

    private JedisPool pool;

    @Before
    public void init() throws IOException {
        super.init();

        // Prepare cache client by jedis (need modification)
        JedisPoolConfig config = new JedisPoolConfig();
        config.setBlockWhenExhausted(true);
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        config.setMaxWaitMillis(5000);

        this.pool = new JedisPool(config, this.cacheServerHost, this.redisServerPort, 5000);
    }

    @Test
    @Override
    public void lock_then_unlock() throws LockFailedException {
        RedisLock lock = null;
        try {
            Jedis jedis =  this.pool.getResource();
            RedisClient client = new RedisClient(jedis);

            lock = new RedisLock(this.testTargetKey, client);

            this.locker.lock(lock);
            LOG.info(String.format("Lock acquired successfully for key[%s]!", this.testTargetKey));

        } finally {
            this.locker.unlock(lock);
            LOG.info(String.format("Lock released successfully for key[%s]!", this.testTargetKey));
        }
    }
}
