package com.pytsoft.cachelock.connector;

import com.pytsoft.cachelock.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by PYT on 2015/6/29.
 */
public interface CacheClient {

    Logger LOG = LoggerFactory.getLogger(CacheClient.class);

    boolean setnx(String key, String value, int expSeconds);

    boolean hsetnx(String key, String field, String value, int expSeconds);

    String get(String key);

    String hget(String key, String field);

    void set(String key, String value, int expSeconds);

    void hset(String key, String field, String value, int expSeconds);

    void del(String key);

    void hdel(String key, String field);
}
