package com.pytsoft.cachelock.util;

/**
 * Created by Administrator on 2015/6/4.
 */
public class LockFailedException extends Exception {
    public LockFailedException(String errMsg) {
        super(errMsg);
    }
}
