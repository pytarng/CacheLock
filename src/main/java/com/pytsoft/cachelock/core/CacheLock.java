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

import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.Constants;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * The {@code CacheLock} abstract class is the distributed implementation of the {@code java.util.concurrent.locks.Lock} interface.
 * <p>
 * There are two kinds of constructor, with two arguments (key and client) and three arguments (key, field, and client)
 * separately, represents for key lock and hash field lock.
 * <p>
 * The lock class and cache client class should be in pairs, and this parent abstract class should not be used directly.
 * For example:
 * <blackquote><pre>
 *     CacheClient redisClient = new RedisClient(jedis);
 *     CacheLock cacheLock = new RedisLock("test_key", redisClient);
 *     try {
 *         locker.lock(cacheLock);
 *     }
 *     finally {
 *         locker.unlock(cacheLock);
 *     }
 * </pre></blackquote>
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.core.RedisLock
 * @see com.pytsoft.cachelock.core.RedisClusterLock
 * @see com.pytsoft.cachelock.core.MemcachedLock
 * @since JDK 1.6
 */
public class CacheLock implements Lock {
	protected String key;
	protected String field;

	protected boolean hashLock = false;
	protected boolean locked = false;

	protected CacheClient client;

	public CacheLock(String key, CacheClient client) {
		this.key = Constants.CACHE_KEY_HEAD_LOCKER + key;
		this.client = client;
	}

	public CacheLock(String key, String field, CacheClient client) {
		this(key, client);
		this.field = Constants.CACHE_KEY_HEAD_LOCKER + field;
		this.hashLock = true;
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
	 *
	 * <p>If the lock is not available then the current thread becomes
	 * disabled for thread scheduling purposes and lies dormant until the
	 * lock has been acquired.
	 */
	@Override
	public void lock() {

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
	 * <p><b>Implementation Considerations</b>
	 *
	 * <p>The ability to interrupt a lock acquisition in some
	 * implementations may not be possible, and if possible may be an
	 * expensive operation.  The programmer should be aware that this
	 * may be the case. An implementation should document when this is
	 * the case.
	 *
	 * <p>An implementation can favor responding to an interrupt over
	 * normal method return.
	 *
	 * <p>A {@code Lock} implementation may be able to detect
	 * erroneous use of the lock, such as an invocation that would
	 * cause deadlock, and may throw an (unchecked) exception in such
	 * circumstances.  The circumstances and the exception type must
	 * be documented by that {@code Lock} implementation.
	 *
	 * @throws InterruptedException
	 * 		if the current thread is
	 * 		interrupted while acquiring the lock (and interruption
	 * 		of lock acquisition is supported)
	 */
	@Override
	public void lockInterruptibly() throws InterruptedException {

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
		return false;
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
	 * <p><b>Implementation Considerations</b>
	 *
	 * <p>The ability to interrupt a lock acquisition in some implementations
	 * may not be possible, and if possible may
	 * be an expensive operation.
	 * The programmer should be aware that this may be the case. An
	 * implementation should document when this is the case.
	 *
	 * <p>An implementation can favor responding to an interrupt over normal
	 * method return, or reporting a timeout.
	 *
	 * <p>A {@code Lock} implementation may be able to detect
	 * erroneous use of the lock, such as an invocation that would cause
	 * deadlock, and may throw an (unchecked) exception in such circumstances.
	 * The circumstances and the exception type must be documented by that
	 * {@code Lock} implementation.
	 *
	 * @param time
	 * 		the maximum time to wait for the lock
	 * @param unit
	 * 		the time unit of the {@code time} argument
	 *
	 * @return {@code true} if the lock was acquired and {@code false}
	 * if the waiting time elapsed before the lock was acquired
	 *
	 * @throws InterruptedException
	 * 		if the current thread is interrupted
	 * 		while acquiring the lock (and interruption of lock
	 * 		acquisition is supported)
	 */
	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	/**
	 * Releases the lock.
	 *
	 * <p><b>Implementation Considerations</b>
	 *
	 * <p>A {@code Lock} implementation will usually impose
	 * restrictions on which thread can release a lock (typically only the
	 * holder of the lock can release it) and may throw
	 * an (unchecked) exception if the restriction is violated.
	 * Any restrictions and the exception
	 * type must be documented by that {@code Lock} implementation.
	 */
	@Override
	public void unlock() {

	}

	/**
	 * Returns a new {@link Condition} instance that is bound to this
	 * {@code Lock} instance.
	 *
	 * <p>Before waiting on the condition the lock must be held by the
	 * current thread.
	 * A call to {@link Condition#await()} will atomically release the lock
	 * before waiting and re-acquire the lock before the wait returns.
	 *
	 * <p><b>Implementation Considerations</b>
	 *
	 * <p>The exact operation of the {@link Condition} instance depends on
	 * the {@code Lock} implementation and must be documented by that
	 * implementation.
	 *
	 * @return A new {@link Condition} instance for this {@code Lock} instance
	 *
	 * @throws UnsupportedOperationException
	 * 		if this {@code Lock}
	 * 		implementation does not support conditions
	 */
	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}
}
