package com.pytsoft.cachelock;

import com.pytsoft.cachelock.beans.RedisLock;
import com.pytsoft.cachelock.connector.RedisClient;
import com.pytsoft.cachelock.util.LockFailedException;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

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
		this.pool = new JedisPool(config, this.cacheServerHost, this.redisServerPort, 5000);
	}

	@Test
	@Override
	public void lock_then_unlock() {
		RedisLock lock = null;
		try {
			Jedis jedis = this.pool.getResource();
			RedisClient client = new RedisClient(jedis);

			lock = new RedisLock(this.testTargetKey, client);

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
