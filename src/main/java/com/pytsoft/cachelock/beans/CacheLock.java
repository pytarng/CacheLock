package com.pytsoft.cachelock.beans;

/**
 * Created by Administrator on 2015/5/4.
 */
public class CacheLock {

    private String key;

    private String field;

    private Object lock;

    private boolean hashLock = false;

    public CacheLock(){};

    public CacheLock(String key, Object lock) {
        this.key = key;
        this.lock = lock;
    }

    public CacheLock(String key, String field, Object lock) {
        this.key = key;
        this.field = field;
        this.lock = lock;
        this.hashLock = true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }
}
