package com.pytsoft.cachelock.util;

/**
 * Created by PYT on 2015/6/30
 */
public class Constants {
    public static final String CACHE_KEY_HEAD_LOCKER = "booking:locker::";
    public static final String NAMESPACE_SEPARATOR = "::";

    public static final long DEFAULT_ACQUIRE_INTERVAL_MS = 100;
    public static final long DEFAULT_LOCK_EXPIRE_MS = 15 * 1000;
    public static final long DEFAULT_ACQUIRE_TIMEOUT_MS = 10 * 1000;
    public static final float DEFAULT_PRIORITY_RATIO = 0.8f;
}
