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

import com.pytsoft.cachelock.beans.CacheLock;
import com.pytsoft.cachelock.beans.RedisClusterLock;
import com.pytsoft.cachelock.connector.RedisClusterClient;
import com.pytsoft.cachelock.util.LockFailedException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Test cases class for {@code LockSmith} for redis cluster.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.LockSmith
 * @since JDK 1.6
 */
public class LockSmithTest_RedisCluster_Jedis extends LockSmithTest {

	private JedisCluster cluster;

	@Before
	public void init() {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		HostAndPort hostAndPort = new HostAndPort(this.cacheServerHost, this.redisServerPort);
		jedisClusterNodes.add(hostAndPort);

		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setBlockWhenExhausted(true);
		config.setMaxTotal(20);
		config.setMaxIdle(10);
		config.setMinIdle(5);
		config.setMaxWaitMillis(5000);

		this.cluster = new JedisCluster(jedisClusterNodes, config);
	}

	@Test
	@Override
	public void lock_then_unlock() {
		CacheLock lock = null;
		try {
			RedisClusterClient client = new RedisClusterClient(this.cluster);

			lock = new RedisClusterLock(this.testTargetKey, client);

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
