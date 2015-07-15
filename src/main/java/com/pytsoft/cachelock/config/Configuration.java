package com.pytsoft.cachelock.config;

/**
 * The {@code Configuration} class is to hold all configuration settings for lock acquisition process.
 * <p>
 * Current content settings are as follows:
 * <ul>
 * <li> initInterval - Sleep interval in milliseconds for lock acquisition process
 * <li> acquireTimeout - Maximum waiting time in milliseconds for lock acquisition process
 * <li> lockExpiration - Lifetime in milliseconds for a lock to stay valid
 * <li> priorityRatio - Sleep interval shrink ratio
 * </ul>
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.util.Constants
 * @since JDK 1.6
 */
public class Configuration {

	private long initInterval;
	private long acquireTimeout;
	private long lockExpiration;
	private float priorityRatio;

	public long getInitInterval() {
		return initInterval;
	}

	public void setInitInterval(long initInterval) {
		this.initInterval = initInterval;
	}

	public long getAcquireTimeout() {
		return acquireTimeout;
	}

	public void setAcquireTimeout(long acquireTimeout) {
		this.acquireTimeout = acquireTimeout;
	}

	public long getLockExpiration() {
		return lockExpiration;
	}

	public void setLockExpiration(long lockExpiration) {
		this.lockExpiration = lockExpiration;
	}

	public float getPriorityRatio() {
		return priorityRatio;
	}

	public void setPriorityRatio(float priorityRatio) {
		this.priorityRatio = priorityRatio;
	}
}
