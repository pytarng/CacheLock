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
3. [Download the source code](https://github.com/pytarng/CacheLock#fork-destination-box) and customize it according to your need.


Maven
---------

Include the following script to your `pom.xml` dependency list:

(Central Repo under Application...)
```
    <dependency>
        <groupId>xxx</groupId>
        <artifactId>xxx</artifactId>
        <version>1.0.0</version>
    </dependency>
```

Coding Samples:
-----------------------

1. Use Jedis lib to acquire lock for key "target" from Redis cache server on host "1.2.3.4" and port 6379:
   ```
      // Generate cache client for Redis server with Jedis lib.
      Jedis jedis = new Jedis("1.2.3.4", 6379);
      RedisClient client = new RedisClient(jedis);
      
      // Generate cache lock obj
      CacheLock lock = new RedisLock("target", client);
      
      // Use locker to lock/unlock target
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
"192.168.1.2", "192.168.1.3", "192.168.1.4" and port 6379:
   ```
      // Generate cache client for Redis cluster with Jedis lib.
      Set<HostAndPort> nodes = new HashSet<HostAndPort>();
      nodes.add(new HostAndPort(192.168.1.1, 6379));
      nodes.add(new HostAndPort(192.168.1.2, 6379));
      nodes.add(new HostAndPort(192.168.1.3, 6379));
      nodes.add(new HostAndPort(192.168.1.4, 6379));
      JedisCluster cluster = new JedisCluster(nodes);
      RedisClusterClient client = new RedisClusterClient(cluster);
      
      // Generate cache lock obj
      CacheLock lock = new RedisClusterLock("lockit", "tField", client);
      
      // Use locker to lock/unlock target
      LockSmith locker = new LockSmith();
      locker.lock(lock);
      try {
         // Operations...
         ......
      } finally {
         locker.unlock(lock);
      }
   ```
3. Use spymemcached lib to acquire lock for key "target" from memcached cache server on host "10.10.10.10" and port 11211:
   ```
      // Generate cache client for memcached server with spymemcached lib.
      MemcachedClient memcachedClient = new MemcachedClient(new InetSocketAddress("10.10.10.10", 11211));
      com.pytsoft.cachelock.connector.MemcachedClient client = new com.pytsoft.cachelock.connector.MemcachedClient(memcachedClient);
      
      // Generate cache lock obj
      CacheLock lock = new MemcachedLock("target", client);
      
      // Use locker to lock/unlock target
      LockSmith locker = new LockSmith();
      locker.lock(lock);
      try {
         // Operations...
         ......
      } finally {
         locker.unlock(lock);
      }
   ```
4. Set detailed configuration for lock object:
   ```
      // Generate cache client...
      ......
      
      // Generate cache lock obj with customized configuration
      Configuration config = new Configuration();
      config.setInitInterval(100);
      config.setLockExpiration(30000);
      config.setPriorityRatio(0.9f);
      CacheLock lock = new RedisLock("target", config);
      
      // Use locker to lock/unlock target
      ......
   ```
5. Acquire lock with customized timeout (ex. 3 seconds):
   ```
      // Generate cache client...
      ......
      
      // Generate cache lock obj...
      ......
      
      // Use locker to lock/unlock target
      LockSmith locker = new LockSmith();
      locker.lock(lock, 3000);
      try {
         // Operations...
         ......
      } finally {
         locker.unlock(lock);
      }
   ```
6. Implement lock object with third-party client lib:
   Cache client class:
   ```
      public class XYZClient implements CacheClient() {
         // Implements all required methods......
         
         @Override
         public boolean setnx(String key, String value, int expSeconds) {
            ......
         }
         
         @Override
         public boolean hsetnx(String key, String field, String value, int expSeconds) {
            ......
         }
         
         @Override
         public String get(String key) {
            ......
         }
         
         ......
      }
   ```
   Usage:
   ```
      // Generate cache client...
      CacheClient client = new XYZClient(......);
      
      // Generate cache lock obj...
      CacheLock lock = new OtherCacheLock("target", client);
      
      // Use locker to lock/unlock target
      LockSmith locker = new LockSmith();
      locker.lock(lock);
      try {
         // Operations...
         ......
      } finally {
         locker.unlock(lock);
      }
   ```
