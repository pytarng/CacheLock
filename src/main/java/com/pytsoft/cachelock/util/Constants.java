package com.pytsoft.cachelock.util;

/**
 * The {@code Constants} class defines constants.
 *
 * @author Ben PY Tarng
 * @since JDK 1.6
 */
public class Constants {

	/**
	 * Default header string for target keys lock.
	 */
	public static final String CACHE_KEY_HEAD_LOCKER = "booking:locker::";

	// Default separators.
	public static final String NAMESPACE_SEPARATOR = "::";
	public static final String DEFAULT_SEPARATOR = "___";

	// Default configuration settings for lock acquisition algorithms.
	/**
	 * Default sleep interval in milliseconds for lock acquisition process
	 */
	public static final long DEFAULT_ACQUIRE_INTERVAL_MS = 100;

	/**
	 * Default lifetime in milliseconds for a lock to stay valid
	 */
	public static final long DEFAULT_LOCK_EXPIRE_MS = 15 * 1000;

	/**
	 * Default maximum waiting time in milliseconds for lock acquisition process
	 */
	public static final long DEFAULT_ACQUIRE_TIMEOUT_MS = 10 * 1000;

	/**
	 * Default sleep interval decreasing ratio to make the thread who waits longer wake up earlier.
	 * <p>
	 * This ratio means the sleep interval decreasing ratio, after each sleep,
	 * the new interval will be the multiple of the previous interval and this ratio.
	 * <p>
	 * For example:
	 * <blackquote><pre>
	 *     Assumes that
	 *     Ratio: 0.8
	 *     Current interval: 1000
	 *     Then
	 *     Second interval: 1000 * 0.8 = 800
	 *     Third interval: 800 * 0.8 = 640
	 *     ......
	 *     Until interval = 0 (which means do not sleep).
	 * </pre></blackquote>
	 *
	 * Notice that the smaller the ratio is, the faster the interval achieves zero,
	 * which mean the larger CPU resources cost.
	 */
	public static final float DEFAULT_PRIORITY_RATIO = 0.8f;
}
