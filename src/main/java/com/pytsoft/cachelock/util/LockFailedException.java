package com.pytsoft.cachelock.util;

/**
 * Created by PYT on 2015/6/30
 */
public class LockFailedException extends Exception {
    public LockFailedException(String errMsg) {
        super(errMsg);
    }
}
