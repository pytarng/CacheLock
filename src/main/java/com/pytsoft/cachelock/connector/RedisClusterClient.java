package com.pytsoft.cachelock.connector;

import com.pytsoft.cachelock.util.KeyUtils;
import redis.clients.jedis.JedisCluster;

/**
 * The {@code RedisClusterClient} class is the Redis-cluster client implementation of {@code CacheClient} with usage of Jedis library.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.connector.CacheClient
 * @since JDK 1.6
 */
public class RedisClusterClient implements CacheClient {

	protected JedisCluster cluster;

	public RedisClusterClient(JedisCluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public boolean setnx(String key, String value, int expSeconds) {
		if (this.cluster.setnx(key, value) == 1) {
			// Add key-value pair expiration settings to redis cluster.
			this.cluster.expire(key, expSeconds);
		}
		return false;
	}

	@Override
	public boolean hsetnx(String key, String field, String value, int expSeconds) {
		if (this.cluster.hsetnx(key, field, value) == 1) {
			return true;
		} else {
			// Expiration setting is not supported in hsetnx case, need further operation.
			// Parse expiration time from stored value in key-value pair.
			String lockValue = this.hget(key, field);
			long expTime = KeyUtils.parseTime(lockValue);

			// Check whether previous set value is already expired.
			if (System.currentTimeMillis() - expTime > expSeconds * 1000) {

				// If already expired, delete field and hset again.
				this.hdel(key, field);
				return this.cluster.hsetnx(key, field, value) == 1;
			}
		}
		return false;
	}

	@Override
	public String get(String key) {
		return this.cluster.get(key);
	}

	@Override
	public String hget(String key, String field) {
		return this.cluster.hget(key, field);
	}

	@Override
	public void del(String key) {
		this.cluster.del(key);
	}

	@Override
	public void hdel(String key, String field) {
		this.cluster.hdel(key, field);
	}
}
