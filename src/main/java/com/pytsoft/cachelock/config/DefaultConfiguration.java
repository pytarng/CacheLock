package com.pytsoft.cachelock.config;

import com.pytsoft.cachelock.util.Constants;

/**
 * The {@code DefaultConfiguration} is the child configuration class with default values.
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.util.Constants
 * @since JDK 1.6
 */
public class DefaultConfiguration extends Configuration {

	public DefaultConfiguration() {
		this.setInitInterval(Constants.DEFAULT_ACQUIRE_INTERVAL_MS);
		this.setAcquireTimeout(Constants.DEFAULT_ACQUIRE_TIMEOUT_MS);
		this.setLockExpiration(Constants.DEFAULT_LOCK_EXPIRE_MS);
		this.setPriorityRatio(Constants.DEFAULT_PRIORITY_RATIO);
	}
}
