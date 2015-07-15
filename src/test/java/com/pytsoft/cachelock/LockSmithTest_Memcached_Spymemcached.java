package com.pytsoft.cachelock;

import com.pytsoft.cachelock.beans.MemcachedLock;
import com.pytsoft.cachelock.util.LockFailedException;
import net.spy.memcached.MemcachedClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Test cases class for {@code LockSmith} for memcached server.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.LockSmith
 * @since JDK 1.6
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
			com.pytsoft.cachelock.connector.MemcachedClient client = new com.pytsoft.cachelock.connector.MemcachedClient(this.memcachedClient);

			lock = new MemcachedLock(this.testTargetKey, client);

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
