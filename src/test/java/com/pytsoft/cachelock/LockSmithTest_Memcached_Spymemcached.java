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

        this.memcachedClient = new MemcachedClient(new InetSocketAddress("192.168.8.213", 11211));
    }

    @Test
    @Override
    public void lock_then_unlock() {
        String lockKey = "TEST_KEY_MEMCACHED_SPYMEMCACHED" + Constants.DEFAULT_SEPARATOR + UUID.randomUUID().toString();
        MemcachedLock lock = null;
        try {
            lock = new MemcachedLock(lockKey, this.memcachedClient);
            this.locker.lock(lock);
            LOG.info(String.format("Lock acquired successfully for key[%s]!", lockKey));

        } catch (LockFailedException e) {
            LOG.error(String.format("Error occurs while trying to acquire lock for key[%s]!", lockKey), e);

        } finally {
            this.locker.unlock(lock);
            LOG.info(String.format("Lock released successfully for key[%s]!", lockKey));
        }
    }
}
