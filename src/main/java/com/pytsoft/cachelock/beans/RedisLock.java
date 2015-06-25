package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.LockFailedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

public class RedisLock {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final long ACQUIRE_INTERVAL_MS = 100;
    private static final long DEFAULT_LOCK_EXPIRE_MS = 15 * 1000;
    private static final long DEFAULT_ACQUIRE_TIMEOUT_MS = 10 * 1000;
    private static final float PRIORITY_RATIO = 0.8f;

    private Jedis jedis;
    private JedisCluster cluster;
    private String lockKey;
    private String lockField;
    private String lockValue;
    private boolean locked = false;
    private boolean hashLock = false;

    private long lockExpireMs = DEFAULT_LOCK_EXPIRE_MS;
    private long acquireTimeoutMs = DEFAULT_ACQUIRE_TIMEOUT_MS;
    private long nextInterval = ACQUIRE_INTERVAL_MS;

    public RedisLock(String lockKey) {
        this.lockKey = Constants.CACHE_KEY_HEAD_LOCKER + lockKey;

        // Generate lock value with expire information.
        long expireTime = System.currentTimeMillis() + this.lockExpireMs + 1;
        this.lockValue = UUID.randomUUID().toString() + Constants.NAMESPACE_SEPARATOR + expireTime;
    }

    public RedisLock(String lockKey, String lockField) {
        this(lockKey);
        this.lockField = Constants.CACHE_KEY_HEAD_LOCKER + lockField;
        this.hashLock = true;
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getLockField() {
        return lockField;
    }

    public String getLockValue() {
        return lockValue;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setLockExpireMs(long lockExpireMs) {
        this.lockExpireMs = lockExpireMs;
    }

    public void setAcquireTimeoutMs(long acquireTimeoutMs) {
        this.acquireTimeoutMs = acquireTimeoutMs;
    }

    public synchronized boolean acquire() throws LockFailedException {
        long begin = System.currentTimeMillis();

        while (true) {
            long current = System.currentTimeMillis();
            if (current - begin > this.acquireTimeoutMs) {
                // already timeout - leave while loop.
                break;
            }

            if (!this.hashLock) {
                if (this.jedis.setnx(this.lockKey, this.lockValue) == 1) {
                    // lock acquired
                    this.locked = true;
                    return true;
                }

                // check whether already own this key.
                String prevLockValue = this.jedis.get(this.lockKey);
                if (StringUtils.equals(prevLockValue, this.lockValue)) {
                    this.locked = true;
                    return true;
                }

                // check whether previous owner already expired.
                long expiredTime = this.parseTime(prevLockValue);
                if (expiredTime < current) {
                    // previous lock is expired, try to acquire!
                    this.jedis.set(this.lockKey, this.lockValue);
                    continue;
                }
            } else {
                if (this.jedis.hsetnx(this.lockKey, this.lockField, this.lockValue) == 1) {
                    // lock acquired
                    this.locked = true;
                    return true;
                }

                // check whether already own this key.
                String prevLockValue = this.jedis.hget(this.lockKey, this.lockField);
                if (StringUtils.equals(prevLockValue, this.lockValue)) {
                    this.locked = true;
                    return true;
                }

                // check whether previous owner already expired.
                long expiredTime = this.parseTime(prevLockValue);
                if (expiredTime < current) {
                    // previous lock is expired, try to acquire!
                    this.jedis.hset(this.lockKey, this.lockField, this.lockValue);
                    continue;
                }
            }

            // wait for the next round.
            try {
                Thread.sleep(this.nextInterval);
                this.nextInterval = (long) (this.nextInterval * PRIORITY_RATIO);
            } catch (InterruptedException e) {
                throw new LockFailedException(String.format("Lock acquiring process for key[%s] interrupted unexpectedly! Failed!", this.lockKey));
            }
        }
        return false;
    }

    public synchronized void release() {
        if (this.locked) {
            if(!this.hashLock) {
                this.jedis.del(this.lockKey);
                this.locked = false;
            } else {
                this.jedis.hdel(this.lockKey, this.lockField);
                this.locked = false;
            }
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
