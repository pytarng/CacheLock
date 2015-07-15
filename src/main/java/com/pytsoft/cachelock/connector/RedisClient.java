package com.pytsoft.cachelock.connector;

import com.pytsoft.cachelock.util.KeyUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by PYT on 2015/6/29.
 */
public class RedisClient implements CacheClient {

    protected Jedis jedis;

    public RedisClient(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public boolean setnx(String key, String value, int expSeconds) {
        if (this.jedis.setnx(key, value) == 1) {
            this.jedis.expire(key, expSeconds);
            return true;
        }
        return false;
    }

    @Override
    public boolean hsetnx(String key, String field, String value, int expSeconds) {
        if (this.jedis.hsetnx(key, field, value) == 1) {
            return true;
        } else {
            // expire is not possible in hsetnx case, need to parse expiration infomation from the lock value.
            String lockValue = this.jedis.hget(key, field);
            long expTime = KeyUtils.parseTime(lockValue);
            // Check whether previous set value is already expired.
            if (System.currentTimeMillis() - expTime > expSeconds * 1000) {
                this.jedis.hdel(key, field);
                return this.jedis.hsetnx(key, field, value) == 1;
            }
        }
        return false;
    }

    @Override
    public String get(String key) {
        return this.jedis.get(key);
    }

    @Override
    public String hget(String key, String field) {
        return this.jedis.hget(key, field);
    }

    @Override
    public void set(String key, String value, int expSeconds) {
        this.jedis.setex(key, expSeconds, value);
    }

    @Override
    public void hset(String key, String field, String value, int expSeconds) {
        // expire is not possible in hset case, need to store in the lock value.
        this.jedis.hset(key, field, value);
    }

    @Override
    public void del(String key) {
        this.jedis.del(key);
    }

    @Override
    public void hdel(String key, String field) {
        this.jedis.hdel(key, field);
    }
}
