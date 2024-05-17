package com.dallxy.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class LocalCache implements ICache<String, Object> {

    private final Cache<String, Object> caffeineLoadingCache;

    public <T> T getIfPresent(String key, Class<T> clazz) {
        return (T) caffeineLoadingCache.getIfPresent(key);
    }

    @Override
    public Object getIfPresent(String key) {
        return caffeineLoadingCache.getIfPresent(key);
    }

    public <T> T get(String key, Class<T> z, Function<String, Object> valueMissingHandler) {
        return z.cast(caffeineLoadingCache.get(key, valueMissingHandler));
    }


    @Override
    public Object get(String key, Function<String, Object> valueMissingHandler) {
        return caffeineLoadingCache.get(key, valueMissingHandler);
    }

    @Override
    public void put(String key, Object value) {
        caffeineLoadingCache.put(key, value);
    }


    @Override
    public void putAll(Map<? extends String, ?> map) {
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
