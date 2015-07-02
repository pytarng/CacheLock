package com.pytsoft.cachelock.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ben on 2015/7/3.
 */
public interface IntervalHandler {

    Logger LOG = LoggerFactory.getLogger(IntervalHandler.class);

    long evalNextInterval(long currentInterval);
}
