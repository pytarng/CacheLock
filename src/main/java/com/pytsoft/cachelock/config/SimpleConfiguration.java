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

import com.pytsoft.cachelock.util.Constants;

/**
 * The {@code SimpleConfiguration} is the child configuration class with base values (without starvation issue handling).
 *
 * @author Ben PY Tarng
 * @see com.pytsoft.cachelock.util.Constants
 * @since JDK 1.6
 */
public class SimpleConfiguration extends DefaultConfiguration {

    public SimpleConfiguration() {
        this.setInitInterval(Constants.DEFAULT_ACQUIRE_INTERVAL_MS);
        this.setLockExpiration(Constants.DEFAULT_LOCK_EXPIRE_MS);
        this.setPriorityRatio(1);
    }
}
