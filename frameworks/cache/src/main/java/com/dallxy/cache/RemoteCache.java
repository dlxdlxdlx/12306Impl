package com.dallxy.cache;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class RemoteCache implements ICache<String> {
    private static final long DEFAULT_TIMEOUT = 6;
    private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.HOURS;

    @Getter
    private final StringRedisTemplate stringRedisTemplate;

    public <T> T getIfPresent(String key, Class<T> clazz) {
        return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), clazz);
    }


    public Object get(String key, Function<String, Object> handler, long timeout, TimeUnit unit) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)).orElseGet(() -> {
            Object v = handler.apply(key);
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(v), timeout, unit);
            return JSONObject.toJSONString(v);
        });
    }

    @Override
    public void put(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
    }

    public void put(String key, Object value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
        stringRedisTemplate.expire(key, timeout, unit);
    }

    @Override
    public <T> T getIfPresent(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key)) ? (T) stringRedisTemplate.opsForValue().get(key) : null;
    }

    @Override
//    remote cache(redis等)的数据存储交给用户决定
    public <V> V get(String key, Function<String, Object> handler) {
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), new TypeReference<V>() {});
        }
        return (V)handler.apply(key);
    }

    @Override
    public <V> void putAll(Map<? extends String, ? extends V> map) {
        map.forEach((k, v) -> stringRedisTemplate.opsForValue().set(k, JSONObject.toJSONString(v)));
    }

    @Override
    public void invalidate(String key) {
        stringRedisTemplate.delete(key);
    }


}
