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

import com.pytsoft.cachelock.core.CacheLock;
import com.pytsoft.cachelock.config.Configuration;
import com.pytsoft.cachelock.config.DefaultConfiguration;
import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.KeyUtils;
import com.pytsoft.cachelock.exception.LockFailedException;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * The {@code LockSmith} class is the key distributed lock manager component in CacheLock project.
 * It supports <a href="http://redis.io/">Redis</a> (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib)
 * , Redis cluster (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib),
 * and <a href="http://memcached.org/">Memcached</a>
 * (with <a href="https://github.com/couchbase/spymemcached">spymemcached</a> lib) by default.
 * <p>
 * <p>Other cache server and client libs can be easily supported by <i>create your own custom client and lock classes</i>
 * , which <b>extends</b> abstract {@code CacheClient} and {@code CacheLock} with abstract methods implemented.
 * <p>
 * <p>Distributed lock can be easily acquired through this class, for example:
 * <blackquote><pre>
 *     LockSmite locker = new LockSmith();
 *     Jedis jedis = jedisPool.getResource();
 *     CacheLock target = new RedisLock("lock_key", jedis);
 *     try {
 *         locker.lock(target);
 *     }
 *     finally {
 *         locker.unlock(target);
 *     }
 * </pre></blackquote>
 * <p>Please make sure that all acquired locks will be unlock after no longer needed. The suggested way is to put the
 * {@code unlock} method call in the <i>finally statement</i>.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.core.CacheLock
 * @see com.pytsoft.cachelock.connector.CacheClient
 * @since JDK 1.6
 */
public class LockSmith {

    /**
     * Acquire target distributed lock from central cache server with default timeout.
     * <p>
     * <p>This is a <b>blocking</b> call with default maximum waiting time, and it blocks
     * while the specific target key (given in {@code CacheLock}) is already acquired by another locker and not returned yet.
     *
     * @param lock
     *         Lock target with all required information (such as key, cache server, ...) inside
     *         (check {@linkplain com.pytsoft.cachelock.core.CacheLock})
     *
     * @throws LockFailedException
     *         If any of the following is true:
     *         <ul>
     *         <li> the target key is {@code null} or with invalid characters
     *         <li> the target key cannot be acquired within maximum waiting time
     *         <li> cache server is unachievable due to network issues
     *         <li> cache server is inaccessible due to permission issues
     *         </ul>
     * @see com.pytsoft.cachelock.core.CacheLock
     * @see com.pytsoft.cachelock.util.Constants
     */
    public void lock(CacheLock lock) throws LockFailedException {

        this.lock(lock, Constants.DEFAULT_ACQUIRE_TIMEOUT_MS);
    }

    /**
     * Acquire target distributed lock from central cache server.
     * <p>
     * <p>This is a <b>blocking</b> call with argument defined maximum waiting time, and it blocks
     * while the specific target key (given in {@code CacheLock}) is already acquired by another locker and not returned yet.
     *
     * @param lock
     *         Lock target with all required information (such as key, cache server, ...) inside
     *         (check {@linkplain com.pytsoft.cachelock.core.CacheLock})
     * @param acquireTimeout
     *         Maximum waiting time in milliseconds for lock acquisition process
     *
     * @throws LockFailedException
     *         If any of the following is true:
     *         <ul>
     *         <li> the target key is {@code null} or with invalid characters
     *         <li> the target key cannot be acquired within maximum waiting time
     *         <li> cache server is unachievable due to network issues
     *         <li> cache server is inaccessible due to permission issues
     *         </ul>
     * @see com.pytsoft.cachelock.core.CacheLock
     */
    public void lock(CacheLock lock, long acquireTimeout) throws LockFailedException {

        try {
            if (!lock.tryLock(acquireTimeout, TimeUnit.MILLISECONDS)) {
                throw new LockFailedException(String.format("Lock acquiring process for lock[%s] failed! Wait time out.", lock));
            }

        } catch (Exception e) {
            throw new LockFailedException(String.format("Lock acquiring process for lock[%s] failed! Reason:[%s]", lock, e.getMessage()), e);
        }
    }

    /**
     * Return acquired target distributed lock back to central cache server.
     *
     * @param lock
     *         Returned lock.
     *
     * @throws LockFailedException
     *         If any of the following is true:
     *         <ul>
     *         <li> cache server is unachievable due to network issues
     *         <li> cache server is inaccessible due to permission issues
     *         </ul>
     * @see com.pytsoft.cachelock.core.CacheLock
     */
    public void unlock(CacheLock lock) throws LockFailedException {
        try {
            lock.unlock();

        } catch (Exception e) {
            throw new LockFailedException(String.format("Lock release for lock[%s] failed! Reason:[%s]", lock, e.getMessage()), e);
        }
    }


}
