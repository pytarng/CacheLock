package com.pytsoft.cachelock.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by Administrator on 2015/7/3.
 */
public class KeyUtils {

	public static String genLockValue(long lockExpire) {
		long expireTime = System.currentTimeMillis() + lockExpire + 1;
		return UUID.randomUUID().toString() + Constants.NAMESPACE_SEPARATOR + expireTime;
	}

	public static long parseTime(String lockValue) {
		if (lockValue != null) {
			String timeStr = StringUtils.split(lockValue, Constants.NAMESPACE_SEPARATOR)[1];
			return Long.parseLong(timeStr);
		}
		return 0;
	}
}
