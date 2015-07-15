package com.pytsoft.cachelock;

import com.pytsoft.cachelock.beans.CacheLock;
import com.pytsoft.cachelock.config.Configuration;
import com.pytsoft.cachelock.config.DefaultConfiguration;
import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.KeyUtils;
import com.pytsoft.cachelock.util.LockFailedException;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@code LockSmith} class is the key distributed lock manager component in CacheLock project.
 * It supports <a href="http://redis.io/">Redis</a> (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib)
 * , Redis cluster (with <a href="https://github.com/xetorthio/jedis">Jedis</a> lib),
 * and <a href="http://memcached.org/">Memcached</a>
 * (with <a href="https://github.com/couchbase/spymemcached">spymemcached</a> lib) by default.
 * <p>
 * Other cache server and client libs can be easily supported by <i>create your own custom client and lock classes</i>
 * , which <b>extends</b> abstract {@code CacheClient} and {@code CacheLock} with abstract methods implemented.
 * <p>
 * Distributed lock can be easily acquired through this class, for example:
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
 * Please make sure that all acquired locks will be unlock after no longer needed. The suggested way is to put the
 * {@code unlock} method call in the <i>finally statement</i>.
 * <p>
 * Detailed configuration settings such as waiting timeout, lock expiration time interval are also supported.
 * The settings can be configured through {@code Configuration} class, for example:
 * <blackquote><pre>
 *     Configuration config = new Configuration();
 *     config.setAcquireTimeout(5000);
 *     config.setLockExpiration(10000);
 *     LockSmith locker = new LockSmith(config);
 * </pre></blackquote>
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.beans.CacheLock
 * @see com.pytsoft.cachelock.connector.CacheClient
 * @see com.pytsoft.cachelock.config.Configuration
 * @since JDK 1.6
 */
public class LockSmith {

	/**
	 * Cache the detailed configuration settings
	 */
	protected Configuration config = new DefaultConfiguration();

	/**
	 * Initializes a newly created {@code LockSmith} object so that it represents
	 * an locker with default configuration settings.
	 */
	public LockSmith() {
		this.config = new DefaultConfiguration();
	}

	/**
	 * Initializes a newly created {@code LockSmith} object so that it represents
	 * an locker with user defined configuration settings as the argument.
	 *
	 * @param config
	 * 		User defined configuration settings
	 *
	 * @see com.pytsoft.cachelock.config.Configuration
	 */
	public LockSmith(Configuration config) {
		this.config = config;
	}

	/**
	 * Acquire target distributed lock from central cache server.
	 * <p>
	 * This is a <b>blocking</b> call with maximum waiting time defined in {@code Configuration}, and it blocks
	 * while the specific target key (given in {@code CacheLock}) is already acquired by another locker and not returned yet.
	 *
	 * @param lock
	 * 		Lock target with all required information (such as key, cache server, ...) inside
	 * 		(check {@linkplain com.pytsoft.cachelock.beans.CacheLock})
	 *
	 * @throws LockFailedException
	 * 		If any of the following is true:
	 * 		<ul>
	 * 		<li> the target key is {@code null} or with invalid characters
	 * 		<li> the target key cannot be acquired within maximum waiting time
	 * 		<li> cache server is unachievable due to network issues
	 * 		<li> cache server is inaccessible due to permission issues
	 * 		</ul>
	 * @see com.pytsoft.cachelock.beans.CacheLock
	 */
	public void lock(CacheLock lock) throws LockFailedException {

		String key = lock.getKey();
		String field = lock.getField();
		boolean isHash = lock.isHashLock();
		CacheClient client = lock.getClient();

		/** Extract detailed lock acquisition algorithm settings from configuration.*/
		long lockExpire = this.config.getLockExpiration();
		long acquireTimeout = this.config.getAcquireTimeout();
		long nextInterval = this.config.getInitInterval();
		float priorityRatio = this.config.getPriorityRatio();

		// Generate lock value with expiration time information.
		int expSeconds = (int) (lockExpire / 1000);
		String lockValue = KeyUtils.genLockValue(lockExpire);

		// Record lock acquisition process begin time.
		long begin = System.currentTimeMillis();
		while (true) {
			long current = System.currentTimeMillis();

			// Check whether already exceeds lock acquisition timeout.
			if (current - begin > acquireTimeout) {
				// Already timeout => raise exception.
				throw new LockFailedException("Lock acquisition timeout!");
			}

			// Try to set lock target key with setnx (set if not existed) command.
			boolean set;
			if (!isHash) {
				set = client.setnx(key, lockValue, expSeconds);
			} else {
				set = client.hsetnx(key, field, lockValue, expSeconds);
			}

			// If return value is True => lock acquired successfully.
			if (set) {
				lock.setLocked(true);
				return;
			}

			// Check whether locker already own this key (handle false-positive issue due to concurrent).
			String prevLockValue;
			if (!isHash) {
				prevLockValue = client.get(key);
			} else {
				prevLockValue = client.hget(key, field);
			}

			// If current value stored in target key is equal to lock value => lock already acquired successfully.
			if (StringUtils.equals(prevLockValue, lockValue)) {
				lock.setLocked(true);
				return;
			}

			// If lock not acquired => sleep a short while and wait for the next round.
			try {
				Thread.sleep(nextInterval);

				// Decrease next sleep interval to increase priority (handle greedy issue).
				nextInterval = (long) (nextInterval * priorityRatio);

			} catch (InterruptedException e) {
				throw new LockFailedException(String.format("Lock acquiring process for key[%s] interrupted unexpectedly! Failed!", key));
			}
		}
	}

	/**
	 * Return acquired target distributed lock back to central cache server.
	 * <p>
	 *
	 * @param lock
	 * 		Returned lock.
	 *
	 * @throws LockFailedException
	 * 		If any of the following is true:
	 * 		<ul>
	 * 		<li> cache server is unachievable due to network issues
	 * 		<li> cache server is inaccessible due to permission issues
	 * 		</ul>
	 * @see com.pytsoft.cachelock.beans.CacheLock
	 */
	public void unlock(CacheLock lock) {
		// Do unlock while lock already acquired.
		if (lock != null && lock.isLocked()) {

			// Delete target key/field to release lock.
			if (!lock.isHashLock()) {
				lock.getClient().del(lock.getKey());
			} else {
				lock.getClient().hdel(lock.getKey(), lock.getField());
			}

			lock.setLocked(false);
		}
	}
}
