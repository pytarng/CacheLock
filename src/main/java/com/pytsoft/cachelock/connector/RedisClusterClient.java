package com.pytsoft.cachelock.connector;

/**
 * Created by PYT on 2015/6/30
 */
public class RedisClusterClient implements CacheClient {
    @Override
    public boolean setnx(String key, String value) {
        return false;
    }

    @Override
    public boolean hsetnx(String key, String field, String value) {
        return false;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String hget(String key, String field) {
        return null;
    }

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void hset(String key, String field, String value) {

    }

    @Override
    public void del(String key) {

    }

    @Override
    public void hdel(String key, String field) {

    }
}
