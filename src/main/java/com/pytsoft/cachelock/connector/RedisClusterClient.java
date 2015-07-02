package com.pytsoft.cachelock.connector;

import redis.clients.jedis.JedisCluster;

/**
 * Created by PYT on 2015/6/30
 */
public class RedisClusterClient implements CacheClient {

    protected JedisCluster cluster;

    public RedisClusterClient(JedisCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean setnx(String key, String value) {
        return this.cluster.setnx(key, value) == 1;
    }

    @Override
    public boolean hsetnx(String key, String field, String value) {
        return this.cluster.hsetnx(key, field, value) == 1;
    }

    @Override
    public String get(String key) {
        return this.cluster.get(key);
    }

    @Override
    public String hget(String key, String field) {
        return this.cluster.hget(key, field);
    }

    @Override
    public void set(String key, String value) {
        this.cluster.set(key, value);
    }

    @Override
    public void hset(String key, String field, String value) {
        this.cluster.hset(key, field, value);
    }

    @Override
    public void del(String key) {
        this.cluster.del(key);
    }

    @Override
    public void hdel(String key, String field) {
        this.cluster.hdel(key, field);
    }
}
