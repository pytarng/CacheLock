package com.pytsoft.cachelock.beans;

import com.pytsoft.cachelock.connector.MemcachedClient;

/**
 * Created by Ben on 2015/7/2.
 */
public class MemcachedLock extends CacheLock {

    public MemcachedLock(String key, net.spy.memcached.MemcachedClient client) {
        super(key);

        this.client = new MemcachedClient(client);
    }

    public MemcachedLock(String key, String field, net.spy.memcached.MemcachedClient client) {
        super(key, field);

        this.client = new MemcachedClient(client);
    }
}
