package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.RedisClient;
import redis.clients.jedis.Jedis;

/**
 * Created by PYT on 2015/6/30
 */
public class RedisLock extends CacheLock {

    public RedisLock(String key, Jedis jedis) {
        super(key);
        this.client = new RedisClient(jedis);
    }

    public RedisLock(String key, String field, Jedis jedis) {
        super(key, field);
        this.client = new RedisClient(jedis);
    }
}
