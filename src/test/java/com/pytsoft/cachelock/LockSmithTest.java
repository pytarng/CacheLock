package com.pytsoft.cachelock;

import com.pytsoft.cachelock.LockSmith;
import com.pytsoft.cachelock.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben on 2015/7/2.
 */
public abstract class LockSmithTest {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected LockSmith locker;

    protected String testTargetKey = "TEST_KEY_MEMCACHED_SPYMEMCACHED" + Constants.DEFAULT_SEPARATOR + UUID.randomUUID().toString();
    protected String cacheServerHost = "192.168.56.101";
    protected int redisServerPort = 6379;
    protected int memcachedServerPort = 11211;

    public void init() throws IOException {
        this.locker = new LockSmith();
    }

    public abstract void lock_then_unlock();
}
