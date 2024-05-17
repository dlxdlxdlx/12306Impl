package com.dallxy.cache;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class LocalCache implements Cache<String, Object>{
    private final LoadingCache<String, Object> caffeineLoadingCache;
    private final RemoteCache remoteCache;

    @Override
    public Object getIfPresent(String key) {
        return caffeineLoadingCache.getIfPresent(key);
    }

    @Override
    public Object get(String key, Function<? super String, ?> loader, long timeout) {
        return null;
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
