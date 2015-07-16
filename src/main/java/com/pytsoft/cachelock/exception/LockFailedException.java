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

package com.pytsoft.cachelock.exception;

/**
 * The {@code LockFailedException} class is the exception for lock acquisition failure.
 *
 * @author Ben PY Tarng
 * @since JDK 1.6
 */
public class LockFailedException extends Exception {
    public LockFailedException(String errMsg) {
        super(errMsg);
    }

    public LockFailedException(String errMsg, Throwable t) {
        super(errMsg, t);
    }
}
