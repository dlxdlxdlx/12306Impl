package com.dallxy.cache;

import java.util.Map;
import java.util.function.Function;

public interface Cache<K, V> {
    /**
     * Retrieves the value associated with the provided key if it is present in the cache.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value to which the specified key is mapped, or null if the cache contains no mapping for the key.
     */
    V getIfPresent(K key);

    /**
     * Associates the specified value with the specified key in the cache.
     * If the cache previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    void put(K key, V value);

    /**
     * Copies all of the mappings from the specified map to the cache.
     * The effect of this call is equivalent to that of calling put(k, v) on the cache once for each mapping from key k to value v in the specified map.
     *
     * @param map Mappings to be stored in the cache.
     */
    void putAll(Map<? extends K, ? extends V> map);

    /**
     * Removes the mapping for a key from the cache if it is present.
     *
     * @param key The key whose mapping is to be removed from the cache.
     */
    void invalidate(K key);

    /**
     * Removes all of the mappings from the cache for the given keys.
     *
     * @param keys The keys whose mappings are to be removed from the cache.
     */
    void invalidateAll(Iterable<? extends K> keys);

    /**
     * Removes all of the mappings from the cache.
     */
    void invalidateAll();
}
