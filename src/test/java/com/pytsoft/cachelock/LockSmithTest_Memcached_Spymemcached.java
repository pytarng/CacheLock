/*
 * Copyright 2015 Ben PY Tarng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pytsoft.cachelock;

import com.pytsoft.cachelock.core.MemcachedLock;
import com.pytsoft.cachelock.exception.LockFailedException;
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
