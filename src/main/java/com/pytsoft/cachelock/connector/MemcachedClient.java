package com.pytsoft.cachelock.connector;

import com.pytsoft.cachelock.util.Constants;
import net.spy.memcached.internal.OperationFuture;

import java.util.concurrent.ExecutionException;

/**
 * Created by PYT on 2015/6/29.
 */
public class MemcachedClient implements CacheClient {

    protected net.spy.memcached.MemcachedClient client;

    public MemcachedClient(net.spy.memcached.MemcachedClient client) {
        this.client = client;
    }

    @Override
    public boolean setnx(String key, String value) {
        OperationFuture<Boolean> result = this.client.add(key, 0, value);

        try {
            return result.get();
        } catch (Exception e) {
            LOG.error("Error occurs while performing setnx for key[%s] and value[%s], reason:[%s]", key, value, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hsetnx(String key, String field, String value) {
        return this.setnx(this.genHashKey(key, field), value);
    }

    @Override
    public String get(String key) {
        return this.client.get(key).toString();
    }

    @Override
    public String hget(String key, String field) {
        return this.get(this.genHashKey(key, field)).toString();
    }

    @Override
    public void set(String key, String value) {
        this.client.set(key, 0, value);
    }

    @Override
    public void hset(String key, String field, String value) {
        this.set(this.genHashKey(key, field), value);
    }

    @Override
    public void del(String key) {
        this.client.delete(key);
    }

    @Override
    public void hdel(String key, String field) {
        this.del(this.genHashKey(key, field));
    }

    private String genHashKey(String key, String field) {
        return key + Constants.DEFAULT_SEPARATOR + field;
    }
}
