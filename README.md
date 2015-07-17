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

You can [download this jar file]() and include it in your project, or use maven to import this lib. 
Otherwise, you can download the source code and customize it according to your need.


Coding Example:
-----------------------



Maven
---------


