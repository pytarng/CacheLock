package com.pytsoft.cachelock;

import com.pytsoft.cachelock.LockSmith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Ben on 2015/7/2.
 */
public abstract class LockSmithTest {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected LockSmith locker;

    public void init() throws IOException {
        this.locker = new LockSmith();
    }

    public abstract void lock_then_unlock();
}
