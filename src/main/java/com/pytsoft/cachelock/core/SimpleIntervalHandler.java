package com.pytsoft.cachelock.core;

/**
 * Created by Ben on 2015/7/3.
 */
public class SimpleIntervalHandler implements IntervalHandler {

    @Override
    public long evalNextInterval(long currentInterval) {
        return currentInterval;
    }
}
