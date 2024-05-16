package com.dallxy.cache;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RemoteCache implements Cache<String,Object>{

    private final StringRedisTemplate stringRedisTemplate;

    public Object getIfPresent(String key, Class<?> clazz){
        return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(key), clazz);
    }

    @Override
    public Object getIfPresent(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {
        map.forEach((k,v) -> stringRedisTemplate.opsForValue().set(k, JSONObject.toJSONString(v)));
    }

    @Override
    public void invalidate(String key) {
        stringRedisTemplate.delete(key);
    }




}
