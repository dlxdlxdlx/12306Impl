package com.dallxy.cache;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class RemoteCache implements ICache<String> {
  private static final long DEFAULT_TIMEOUT = 6;
  private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.HOURS;

  private final StringRedisTemplate stringRedisTemplate;

  public <T> T getIfPresent(String key, Class<T> clazz) {
    return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), clazz);
  }

  public <V> V get(String key, Function<String, V> handler, long timeout, TimeUnit unit) {
    if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
      return JSON.parseObject(
          stringRedisTemplate.opsForValue().get(key), new TypeReference<V>() {});
    }
    V value = handler.apply(key);
    stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout, unit);
    return value;
  }

  @Override
  public void put(String key, Object value) {
    stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
  }

  public <V> void put(String key, V value, long timeout, TimeUnit unit) {
    stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
    stringRedisTemplate.expire(key, timeout, unit);
  }

  @Override
  public <T> T getIfPresent(String key) {
    return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))
        ? (T) stringRedisTemplate.opsForValue().get(key)
        : null;
  }

  @Override
  //    remote cache(redis等)的数据存储交给用户决定
  public <V> V get(String key, Function<String, Object> handler) {
    if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
      return JSONObject.parseObject(
          stringRedisTemplate.opsForValue().get(key), new TypeReference<V>() {});
    }
    V value = (V) handler.apply(key);
    stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    return value;
    //        return (V)handler.apply(key);
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
