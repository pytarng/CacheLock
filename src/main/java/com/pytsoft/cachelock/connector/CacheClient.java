package com.pytsoft.cachelock.connector;

/**
 * Created by PYT on 2015/6/29.
 */
public interface CacheClient {

    boolean setnx(String key, String value);

    boolean hsetnx(String key, String field, String value);

    String get(String key);

    String hget(String key, String field);

    void set(String key, String value);

    void hset(String key, String field, String value);

    void del(String key);

    void hdel(String key, String field);
}
