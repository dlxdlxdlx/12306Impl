package com.dallxy.cache.handler;


public interface ValueMissingHandler<K,V> {
    V handler(K value);
}
