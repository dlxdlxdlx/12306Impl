package com.dallxy.cache;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
@SuppressWarnings("unchecked")
public class LocalCache implements ICache<String> {

    private final Cache<String, Object> caffeineLoadingCache;

    @Override
    public <V> V getIfPresent(String key) {
        return (V) caffeineLoadingCache.getIfPresent(key);
    }

    @Override
    public <V> V get(String key, Function<String, Object> handler) {
        return (V) Optional.ofNullable(caffeineLoadingCache.getIfPresent(key))
                .orElseGet(() -> {
                    String jsonResult = (String) handler.apply(key);
                    V value = JSON.parseObject(jsonResult, new TypeReference<V>() {
                    });
                    caffeineLoadingCache.put(key, value);
                    return value;
                });
    }


    @Override
    public <V> void put(String key, V value) {
        caffeineLoadingCache.put(key, value);
    }

    @Override
    public <V> void putAll(Map<? extends String, ? extends V> map) {
        caffeineLoadingCache.putAll(map);
    }

    @Override
    public void invalidate(String key) {
        caffeineLoadingCache.invalidate(key);
    }




    public void invalidateAll(Iterable<? extends String> keys) {
        caffeineLoadingCache.invalidateAll(keys);
    }

    public void invalidateAll() {
        caffeineLoadingCache.invalidateAll();
    }


}
