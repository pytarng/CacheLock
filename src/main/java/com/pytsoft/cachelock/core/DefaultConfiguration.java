package com.pytsoft.cachelock.core;

import com.pytsoft.cachelock.util.Constants;

/**
 * Created by Ben on 2015/6/30.
 */
public class DefaultConfiguration extends Configuration {

    public DefaultConfiguration() {
        this.configMap.put(INIT_INTERVAL, Constants.DEFAULT_ACQUIRE_INTERVAL_MS);
        this.configMap.put(ACQUIRE_TIMEOUT, Constants.DEFAULT_ACQUIRE_TIMEOUT_MS);
        this.configMap.put(LOCK_EXPIRATION, Constants.DEFAULT_LOCK_EXPIRE_MS);
        this.configMap.put(PRIORITY_RATIO, Constants.DEFAULT_PRIORITY_RATIO);
    }
}
