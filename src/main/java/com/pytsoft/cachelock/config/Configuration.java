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

package com.pytsoft.cachelock.config;

/**
 * The {@code Configuration} class is to hold all configuration settings for lock acquisition process.
 * <p>
 * Current content settings are as follows:
 * <ul>
 * <li> initInterval - Sleep interval in milliseconds for lock acquisition process
 * <li> acquireTimeout - Maximum waiting time in milliseconds for lock acquisition process
 * <li> lockExpiration - Lifetime in milliseconds for a lock to stay valid
 * <li> priorityRatio - Sleep interval decreasing ratio
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
