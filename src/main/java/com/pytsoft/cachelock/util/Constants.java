package com.pytsoft.cachelock.util;

public class Constants {
    public static final String QUEUE_ID_ORDER_REQUEST_WAITING = "order_request_waitings";
    public static final String QUEUE_ID_ORDER_REQUEST_SUCCESS = "order_request_done_need_arrange";

    public static final String CACHE_KEY_HEAD_INDEXING_ROOM_STATE = "indexing:room:state:keys::";
    public static final String CACHE_KEY_HEAD_INDEXING_ROOM_NUMBER = "indexing:room:number:keys::";
    public static final String CACHE_KEY_HEAD_INDEXING_HOTEL_NUMBER = "indexing:hotel:number:keys::";
    public static final String CACHE_KEY_HEAD_ROOM_STATE = "room:state::";
    public static final String CACHE_KEY_HEAD_ROOM_NUMBER = "room:available:number::";
    public static final String CACHE_KEY_HEAD_HOTEL_NUMBER = "hotel:available:number::";

    public static final String CACHE_KEY_HEAD_LOCKER = "booking:locker::";

    public static final String CACHE_HASH_KEY_ORDER_REQUEST = "booking:hash:order:request";
    public static final String CACHE_HASH_KEY_DONE_ORDER_REQUEST = "booking:hash:done:order:request";
    public static final String CACHE_HASH_FIELD_HEAD_ORDER_REQUEST = "order:request::";
    public static final String CACHE_KEY_NEW_ORDER_REQUEST = "new::order:request::";
    public static final String CACHE_KEY_CURRENT_ORDER_NUMBER = "current::order::number";

    public static final long DEFAULT_WORKER_INTERVAL = 1000;

    public static final int UNKNOWN_NUMBER = -1;
    public static final String UNKNOWN_STRING = "N/A";
    public static final String NAMESPACE_SEPARATOR = "::";

    public static final String IP_RANGE_TOKEN = "-";
    public static final String IP_SEP_TOKEN = ".";

    public static final String DEFAULT_SEPARATOR = ",";
    public static final String ROOMKEY_WILDCARD = "[a-zA-z0-9_-]*";

    public static final String CACHE_SERVER_IP_LIST_KEY = "cache_server_ip_list";
    public static final String CACHE_SERVER_PORT_KEY = "cache_server_port";
    public static final String DATABASE_URL_KEY = "database_url";
    public static final String ZOOKEEPER_CONNECT_KEY = "zookeeper_connect";
    public static final String KAFKA_PRODUCER_METADATA_BROKER_LIST_KEY = "kafka_producer_metadata_broker_list";
    public static final String ETCD_HOST_KEY = "ETCD_HOST";

    public static final int DEFAULT_INDEXING_DAYS = 40;

    public static final long MS_FOR_ONE_DAY = 24 * 60 * 60 * 1000;
}
