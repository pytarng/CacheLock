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
import com.pytsoft.cachelock.exception.LockFailedException;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.KeyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * The {@code CacheLock} abstract class is the distributed implementation of the {@code java.util.concurrent.locks.Lock} interface.
 *
 * <p>The lock target key, field, and user defined configurations are defined on constructor. If no user defined
 * configuration is present, default configuration will be used.</p>
 *
 * <p>The lock class and cache client class should be in pairs, and this parent abstract class should not be used directly.
 * For example:
 * <blockquote><pre>
 *     CacheClient redisClient = new RedisClient(jedis);
 *     CacheLock cacheLock = new RedisLock("test_key", redisClient);
 *     locker.lock(cacheLock);
 *     try {
 *         // Operations...
 *     }
 *     finally {
 *         locker.unlock(cacheLock);
 *     }
 * </pre></blockquote>
 *
 * <p>Detailed configuration settings such as lock expiration time interval, sleep interval are also supported.
 * The settings can be configured through {@code Configuration} class, for example:
 * <blockquote><pre>
 *     Configuration config = new Configuration();
 *     config.setLockExpiration(10000);
 *     config.setInitInterval(200);
 *     CacheLock cacheLock = new RedisLock("test_key", redisClient, config);
 * </pre></blockquote>
 *
 * @author Ben PY Tarng
 * @see java.util.concurrent.locks.Lock
 * @see com.pytsoft.cachelock.config.Configuration
 * @see com.pytsoft.cachelock.core.RedisLock
 * @see com.pytsoft.cachelock.core.RedisClusterLock
 * @see com.pytsoft.cachelock.core.MemcachedLock
 * @since 1.0.0
 */
public abstract class CacheLock implements Lock {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected String key;
    protected String field;
    protected Configuration config;

    protected boolean hashLock = false;
    protected boolean locked = false;

    protected CacheClient client;

    public CacheLock(String key, CacheClient client) {
        this.key = Constants.CACHE_KEY_HEAD_LOCKER + key;
        this.client = client;
    }

    public CacheLock(String key, CacheClient client, Configuration config) {
        this(key, client);
        this.config = config;
    }

    public CacheLock(String key, String field, CacheClient client) {
        this(key, client);
        this.field = Constants.CACHE_KEY_HEAD_LOCKER + field;
        this.hashLock = true;
    }

    public CacheLock(String key, String field, CacheClient client, Configuration config) {
        this(key, field, client);
        this.config = config;
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

    /**
     * Acquires the lock.

     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     */
    @Override
    public void lock() {
        try {
            this.tryLock(10, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            LOG.error(String.format("Unexpected interrupted exception!", e));
        }
    }

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     *
     * <p>Acquires the lock if it is available and returns immediately.
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * one of two things happens:
     *
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported.
     * </ul>
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
     * lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * @throws InterruptedException
     *         if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported)
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.tryLock(10, TimeUnit.DAYS);
    }

    /**
     * Acquires the lock only if it is free at the time of invocation.
     *
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     *
     * <p>A typical usage idiom for this method would be:
     * <pre> {@code
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     *
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     *
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    @Override
    public boolean tryLock() {
        try {
            return this.tryLock(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error(String.format("Unexpected interrupted exception!", e));
            return false;
        }
    }

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     *
     * <p>If the lock is available this method returns immediately
     * with the value {@code true}.
     * If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported; or
     * <li>The specified waiting time elapses
     * </ul>
     *
     * <p>If the lock is acquired then the value {@code true} is returned.
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.
     * If the time is
     * less than or equal to zero, the method will not wait at all.
     *
     * @param time
     *         the maximum time to wait for the lock
     * @param unit
     *         the time unit of the {@code time} argument
     *
     * @return {@code true} if the lock was acquired and {@code false}
     * if the waiting time elapsed before the lock was acquired
     *
     * @throws InterruptedException
     *         if the current thread is interrupted
     *         while acquiring the lock (and interruption of lock
     *         acquisition is supported)
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // Generate lock value with expiration time information.
        long lockExpire = this.config.getLockExpiration();
        int expSeconds = (int) (lockExpire / 1000);
        String lockValue = KeyUtils.genLockValue(lockExpire);

        // Get lock acquisition process algorithm detailed settings from config.
        long nextInterval = this.config.getInitInterval();
        float priorityRatio = this.config.getPriorityRatio();

        // Record lock acquisition process begin time.
        long acquireTimeout = TimeUnit.MILLISECONDS.convert(time, unit);
        long begin = System.currentTimeMillis();
        while (true) {
            long current = System.currentTimeMillis();

            // Check whether already exceeds lock acquisition timeout.
            if (current - begin > acquireTimeout) {
                // Already timeout.
                LOG.error(String.format("Time out! Acquire key for target [%s, %s] failed.", this.key, this.field));
                return false;
            }

            // Try to set lock target key with setnx (set if not existed) command.
            boolean set;
            if (!this.hashLock) {
                set = this.client.setnx(this.key, lockValue, expSeconds);
            } else {
                set = this.client.hsetnx(this.key, this.field, lockValue, expSeconds);
            }

            // If return value is True => lock acquired successfully.
            if (set) {
                this.locked = true;
                return true;
            }

            // Check whether locker already own this key (handle false-positive issue due to concurrent).
            String prevLockValue;
            if (!this.hashLock) {
                prevLockValue = this.client.get(this.key);
            } else {
                prevLockValue = this.client.hget(this.key, this.field);
            }

            // If current value stored in target key is equal to lock value => lock already acquired successfully.
            if (StringUtils.equals(prevLockValue, lockValue)) {
                this.locked = true;
                return true;
            }

            // If lock not acquired => sleep a short while and wait for the next round.
            try {
                Thread.sleep(nextInterval);

                // Decrease next sleep interval to increase priority (handle starvation issue).
                nextInterval = (long) (nextInterval * priorityRatio);

            } catch (InterruptedException e) {
                LOG.error(String.format("Lock acquiring process for target [%s, %s] interrupted unexpectedly! Failed!", this.key, this.field));
                return false;
            }
        }
    }

    /**
     * Releases the lock.
     */
    @Override
    public void unlock() {
        // Do unlock while lock already acquired.
        if (this.locked) {

            // Delete target key/field to release lock.
            if (!this.hashLock) {
                this.client.del(this.key);
            } else {
                this.client.hdel(this.key, this.field);
            }

            this.locked = false;
        }
    }

    /**
     * Returns a new {@link Condition} instance that is bound to this
     * {@code Lock} instance.
     *
     * <p>This lock does not support conditions.
     *
     * @return A new {@link Condition} instance for this {@code Lock} instance
     *
     * @throws UnsupportedOperationException
     *         if this {@code Lock}
     *         implementation does not support conditions
     */
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "CacheLock{" +
                "key='" + key + '\'' +
                ", field='" + field + '\'' +
                ", config=" + config +
                '}';
    }
}
