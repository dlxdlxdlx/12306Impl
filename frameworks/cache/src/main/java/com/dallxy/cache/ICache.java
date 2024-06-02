package com.dallxy.cache;



import java.util.Map;
import java.util.function.Function;

public interface ICache<K extends String> {



    <T> T getIfPresent(String key);



    <V> V get(String key, Function<String, Object> handler);

    <V> void put(K key, V value);


    <V> void putAll(Map<? extends K, ? extends V> map);


    void invalidate(K key);



}
