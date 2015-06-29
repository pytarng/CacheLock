package com.pytsoft.cachelock.core;

import com.pytsoft.cachelock.beans.CacheLock;
import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.LockFailedException;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by PYT on 2015/6/30
 */
public class LockSmith {

	protected long lockExpire = Constants.DEFAULT_LOCK_EXPIRE_MS;
	protected long acquireTimeout = Constants.DEFAULT_ACQUIRE_TIMEOUT_MS;
	protected long nextInterval = Constants.DEFAULT_ACQUIRE_INTERVAL_MS;

	public void lock(CacheLock lock) throws LockFailedException {

		String key = lock.getKey();
		String field = lock.getField();
		boolean locked = false;

		long begin = System.currentTimeMillis();

		CacheClient client = lock.getClient();

		// Generate lock value with expire information.
		long expireTime = System.currentTimeMillis() + lockExpire + 1;
		String lockValue = UUID.randomUUID().toString() + Constants.NAMESPACE_SEPARATOR + expireTime;


		while (true) {
			long current = System.currentTimeMillis();
			if (current - begin > acquireTimeout) {
				// already timeout.
				throw new LockFailedException("Lock time out!");
			}

			if (!lock.isHashLock()) {
				if (client.setnx(key, lockValue)) {
					// lock acquired
					lock.setLocked(true);
					return;
				}

				// check whether already own this key.
				String prevLockValue = client.get(key);
				if (StringUtils.equals(prevLockValue, lockValue)) {
					lock.setLocked(true);
					return;
				}

				// check whether previous owner already expired.
				long expiredTime = this.parseTime(prevLockValue);
				if (expiredTime < current) {
					// previous lock is expired, try to acquire!
					client.set(key, lockValue);
					continue;
				}
			} else {
				if (client.hsetnx(key, field, lockValue)) {
					// lock acquired
					lock.setLocked(true);
					return;
				}

				// check whether already own this key.
				String prevLockValue = client.hget(key, field);
				if (StringUtils.equals(prevLockValue, lockValue)) {
					lock.setLocked(true);
					return;
				}

				// check whether previous owner already expired.
				long expiredTime = parseTime(prevLockValue);
				if (expiredTime < current) {
					// previous lock is expired, try to acquire!
					client.hset(key, field, lockValue);
					continue;
				}
			}

			// wait for the next round.
			try {
				Thread.sleep(nextInterval);
				nextInterval = (long) (nextInterval * Constants.DEFAULT_PRIORITY_RATIO);
			} catch (InterruptedException e) {
				throw new LockFailedException(String.format("Lock acquiring process for key[%s] interrupted unexpectedly! Failed!", key));
			}
		}
	}

	public void unlock(CacheLock lock) {
		if (lock.isLocked()) {
			if (!lock.isHashLock()) {
				lock.getClient().del(lock.getKey());
			} else {
				lock.getClient().hdel(lock.getKey(), lock.getField());
			}
			lock.setLocked(false);
		}
	}

	private long parseTime(String lockValue) {
		if(lockValue != null) {
			String timeStr = StringUtils.split(lockValue, Constants.NAMESPACE_SEPARATOR)[1];
			return Long.parseLong(timeStr);
		}
		return 0;
	}

}
