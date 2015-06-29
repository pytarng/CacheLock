package com.pytsoft.cachelock.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 2015/6/30.
 */
public class Configuration {

    public static final String INIT_INTERVAL = "Property::Initial_Interval";
    public static final String ACQUIRE_TIMEOUT = "Property::Lock_Acquire_Timeout";
    public static final String LOCK_EXPIRATION = "Property::Lock_Expiration";
    public static final String PRIORITY_RATIO = "Property::Priority_Ratio";

    protected Map<String, Object> configMap = new HashMap<>();

    public void addProperty(String property, Object value) {
        this.configMap.put(property, value);
    }

    public Object getProperty(String property) {
        return this.configMap.get(property);
    }

    public void setInitInterval(long initInterval) {
        this.configMap.put(INIT_INTERVAL, initInterval);
    }

    public void setAcquireTimeout(long acquireTimeout) {
        this.configMap.put(ACQUIRE_TIMEOUT, acquireTimeout);
    }

    public void setLockExpiration(long lockExpiration) {
        this.configMap.put(LOCK_EXPIRATION, lockExpiration);
    }

    public void setPriorityRatio(float priorityRatio) {
        this.configMap.put(PRIORITY_RATIO, priorityRatio);
    }
}
