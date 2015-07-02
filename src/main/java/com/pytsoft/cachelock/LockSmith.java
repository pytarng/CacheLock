package com.pytsoft.cachelock;

import com.pytsoft.cachelock.beans.CacheLock;
import com.pytsoft.cachelock.connector.CacheClient;
import com.pytsoft.cachelock.core.Configuration;
import com.pytsoft.cachelock.core.DefaultConfiguration;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.LockFailedException;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by PYT on 2015/6/30
 */
public class LockSmith {

    protected Configuration config = new DefaultConfiguration();

    public LockSmith() {
        this.config = new DefaultConfiguration();
    }

    public LockSmith(Configuration config) {
        this.config = config;
    }

    public void lock(CacheLock lock) throws LockFailedException {

        String key = lock.getKey();
        String field = lock.getField();
        boolean isHash = lock.isHashLock();

        long lockExpire = (long) this.config.getProperty(Configuration.LOCK_EXPIRATION);
        long acquireTimeout = (long) this.config.getProperty(Configuration.ACQUIRE_TIMEOUT);
        long nextInterval = (long) this.config.getProperty(Configuration.INIT_INTERVAL);
        float priorityRatio = (float) this.config.getProperty(Configuration.PRIORITY_RATIO);

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

            boolean set;
            if (!isHash) {
                set = client.setnx(key, lockValue);
            } else {
                set = client.hsetnx(key, field, lockValue);
            }
            if (set) {
                // lock acquired
                lock.setLocked(true);
                return;
            }

            // check whether already own this key.
            String prevLockValue;
            if (!isHash) {
                prevLockValue = client.get(key);
            } else {
                prevLockValue = client.hget(key, field);
            }
            if (StringUtils.equals(prevLockValue, lockValue)) {
                lock.setLocked(true);
                return;
            }

            // check whether previous owner already expired.
            long expiredTime = this.parseTime(prevLockValue);
            if (expiredTime < current) {
                // previous lock is expired, try to acquire!
                if (!isHash) {
                    client.set(key, lockValue);
                } else {
                    client.hset(key, field, lockValue);
                }
                continue;
            }

            // wait for the next round.
            try {
                Thread.sleep(nextInterval);
                nextInterval = (long) (nextInterval * priorityRatio);
            } catch (InterruptedException e) {
                throw new LockFailedException(String.format("Lock acquiring process for key[%s] interrupted unexpectedly! Failed!", key));
            }
        }
    }

    public void unlock(CacheLock lock) {
        if (lock != null && lock.isLocked()) {
            if (!lock.isHashLock()) {
                lock.getClient().del(lock.getKey());
            } else {
                lock.getClient().hdel(lock.getKey(), lock.getField());
            }
            lock.setLocked(false);
        }
    }

    private long parseTime(String lockValue) {
        if (lockValue != null) {
            String timeStr = StringUtils.split(lockValue, Constants.NAMESPACE_SEPARATOR)[1];
            return Long.parseLong(timeStr);
        }
        return 0;
    }

}
