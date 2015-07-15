package com.pytsoft.cachelock.util;

/**
 * The {@code LockFailedException} class is the exception for lock acquisition failure.
 *
 * @author Ben PY Tarng
 *
 * @since JDK 1.6
 */
public class LockFailedException extends Exception {
    public LockFailedException(String errMsg) {
        super(errMsg);
    }
}
