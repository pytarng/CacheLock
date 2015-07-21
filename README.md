CacheLock 
=========

`Cache Lock` is a distributed and scalable Java lock management tool which can support almost `all` kinds of cache server.

It supports [Redis](http://redis.io/) and [memcached](http://memcached.org/) cache servers by default, based on [Jedis](https://github.com/xetorthio/jedis) and [spymemcached](https://github.com/couchbase/spymemcached) client libraries. Other types of cache servers and client libraries can be easily supported by customized classes implementation.

![Figure_01](https://cloud.githubusercontent.com/assets/2408906/8738642/e34699f6-2c64-11e5-93c7-3f0a054cc284.png)

Licensed under the Apache License 2.0.


Features:
========

* Distributed implementation of reentrant `java.util.concurrent.locks.Lock`
* Support `Redis and memcached servers` by default.
* Support `Jedis and spymemcached libraries` by default.
* Support `timeout` mechanism for target lock acquisition
* Support `TTL` setting for locks' existence on cache server
* `Dynamic interval algorithm` to deal with starvation issue
* Support `all` kinds of cache client library with simple client class implementation
* Support `all` kinds of cache server with simple corresponding client class implementation.
* Thread-safe implementation


Recent Releases:
------------------------

####Trunk: current development branch.


####17-July 2015 - version 1.0.0 released
First stable version.



Usage:
======

How to Include:
---------------------

1. [Download this jar file](https://github.com/pytarng/CacheLock/blob/mvn-repo/com/pytsoft/CacheLock/1.0.0/CacheLock-1.0.0.jar?raw=true) and include it in your project, and then `find the following dependent libs manually`:
    * [commons-lang3](http://mvnrepository.com/artifact/org.apache.commons/commons-lang3)
    * [commons-logging](http://mvnrepository.com/artifact/commons-logging/commons-logging)
    * [slf4j-api](http://mvnrepository.com/artifact/org.slf4j/slf4j-api)
    * [slf4j-binding](http://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12)
    * [jedis](http://mvnrepository.com/artifact/redis.clients/jedis)
    * [spymemcached](http://mvnrepository.com/artifact/net.spy/spymemcached)
2. Use `maven` to import this lib (see the instructions below). 
3. Download the `source code` and customize it according to your need.


Maven
---------

Include the following script to your `pom.xml` dependency list:

(Central Repo under Application...)

    <dependency>
        <groupId>xxx</groupId>
        <artifactId>xxx</artifactId>
        <version>1.0.0</version>
    </dependency>


Coding Samples:
-----------------------

1. Use Jedis lib to acquire lock for key "target" from Redis cache server on host "1.2.3.4" and port 6379:
```
    // Generate jedis client
    Jedis jedis = new Jedis("1.2.3.4", 6379);
    RedisClient client = new RedisClient(jedis);
    
    RedisLock lock = new RedisLock("target", client);
    LockSmith locker = new LockSmith();
    locker.lock(lock);
    try {
        // Operations...
        ......
    } finally {
        locker.unlock(lock);
    }
```

2. Use Jedis lib to acquire lock for key "lockit" and field "tField" from Redis cache cluster on hosts "192.168.1.1", 
"192.168.1.2", "192.168.1.3", "192.168.1.4":
