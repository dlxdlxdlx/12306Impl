package com.dallxy.cache;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class RemoteCache implements ICache<String, Object> {
    private static final long DEFAULT_TIMEOUT = 6;
    private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.HOURS;
    private final StringRedisTemplate stringRedisTemplate;

    public <T> T getIfPresent(String key, Class<T> clazz) {
        return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), clazz);
    }

    @Override
    public Object getIfPresent(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<?> clazz, Function<String, Object> valueMissingHandler) {
        return (T) get(key, valueMissingHandler);
    }

    @Override
    public Object get(String key, Function<String, Object> valueMissingHandler) {
        return get(key, valueMissingHandler, DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
    }

    public Object get(String key, Function<String, Object> valueMissingHandler, long timeout, TimeUnit unit) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key)).orElseGet(() -> {
            Object v = valueMissingHandler.apply(key);
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
    public void putAll(Map<? extends String, ?> map) {
        map.forEach((k, v) -> stringRedisTemplate.opsForValue().set(k, JSONObject.toJSONString(v)));
    }

    @Override
    public void invalidate(String key) {
        stringRedisTemplate.delete(key);
    }


}
