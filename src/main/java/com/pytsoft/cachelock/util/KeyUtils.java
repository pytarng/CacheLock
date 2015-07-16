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

package com.pytsoft.cachelock.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * The {@code KeyUtils} class is the lock key / value handling utility.
 *
 * @author Ben PY Tarng
 * @since JDK 1.6
 */
public class KeyUtils {

    protected static Logger LOG = LoggerFactory.getLogger(KeyUtils.class);

    /**
     * Generate lock value depends on expiration time to store in the target lock key.
     * <p>
     * <p>The value is composed of a random UUID and expiration time separated by default namespace separator.
     *
     * @param lockExpire
     *         Lock expiration time in milliseconds
     *
     * @return Lock value as string
     */
    public static String genLockValue(long lockExpire) {
        long expireTime = System.currentTimeMillis() + lockExpire + 1;
        return UUID.randomUUID().toString() + Constants.NAMESPACE_SEPARATOR + expireTime;
    }

    /**
     * Parse lock expiration time from lock value stored in lock key.
     * <p>
     * <p>If parameter is null or with invalid format => return 0.
     *
     * @param lockValue
     *         Lock value stored in lock key
     *
     * @return Lock expiration time in milliseconds
     */
    public static long parseTime(String lockValue) {
        if (lockValue != null) {
            try {
                String timeStr = StringUtils.split(lockValue, Constants.NAMESPACE_SEPARATOR)[1];
                return Long.parseLong(timeStr);
            } catch (Exception e) {
                LOG.warn(String.format("Input lock value[%s] parsed failed, reason:[%s], will return 0.", lockValue, e.getMessage()));
                return 0;
            }
        }
        return 0;
    }
}
