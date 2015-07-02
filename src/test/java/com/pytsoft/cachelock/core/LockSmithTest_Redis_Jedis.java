package com.pytsoft.cachelock.core;

import com.pytsoft.cachelock.beans.RedisLock;
import com.pytsoft.cachelock.util.Constants;
import com.pytsoft.cachelock.util.LockFailedException;
import net.spy.memcached.MemcachedClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by Administrator on 2015/6/30.
 */

public class LockSmithTest_Redis_Jedis extends LockSmithTest {

	private JedisPool pool;

	@Before
	public void init() throws IOException {
		super.init();

		// Prepare cache client by jedis (need modification)
		JedisPoolConfig config = new JedisPoolConfig();
		config.setBlockWhenExhausted(true);
		config.setMaxTotal(20);
		config.setMaxIdle(10);
		config.setMinIdle(5);
		config.setMaxWaitMillis(5000);
		this.pool = new JedisPool(config, "192.168.8.213", 6379, 5000);
	}

	@Test
	@Override
	public void lock_then_unlock() {
		String lockKey = "TEST_KEY_REDIS_JEDIS" + Constants.DEFAULT_SEPARATOR + UUID.randomUUID().toString();
		RedisLock lock = null;
		try {
			Jedis jedis = this.pool.getResource();
			lock = new RedisLock(lockKey, jedis);
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
