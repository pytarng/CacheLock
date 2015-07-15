package com.pytsoft.cachelock;

import com.pytsoft.cachelock.LockSmithTest;
import com.pytsoft.cachelock.beans.MemcachedLock;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.LockFailedException;
import net.spy.memcached.MemcachedClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by Ben on 2015/7/2.
 */
public class LockSmithTest_Memcached_Spymemcached extends LockSmithTest {

    private MemcachedClient memcachedClient;

    @Before
    public void init() throws IOException {
        super.init();

        this.memcachedClient = new MemcachedClient(new InetSocketAddress(this.cacheServerHost, this.memcachedServerPort));
    }

    @Test
    @Override
    public void lock_then_unlock() {
        MemcachedLock lock = null;
        try {
            lock = new MemcachedLock(this.testTargetKey, this.memcachedClient);
            this.locker.lock(lock);
            LOG.info(String.format("Lock acquired successfully for key[%s]!", this.testTargetKey));

        } catch (LockFailedException e) {
            LOG.error(String.format("Error occurs while trying to acquire lock for key[%s]!", this.testTargetKey), e);

        } finally {
            this.locker.unlock(lock);
            LOG.info(String.format("Lock released successfully for key[%s]!", this.testTargetKey));
        }
    }
}
