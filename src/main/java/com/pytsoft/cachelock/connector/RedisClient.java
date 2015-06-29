package com.pytsoft.cachelock.connector;

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
    public boolean setnx(String key, String value) {
        return this.jedis.setnx(key, value) == 1;
    }

    @Override
    public boolean hsetnx(String key, String field, String value) {
        return this.jedis.hsetnx(key, field, value) == 1;
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
    public void set(String key, String value) {
        this.jedis.set(key, value);
    }

    @Override
    public void hset(String key, String field, String value) {
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
