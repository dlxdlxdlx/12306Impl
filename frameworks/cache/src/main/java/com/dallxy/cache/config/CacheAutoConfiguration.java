package com.dallxy.cache.config;

import com.dallxy.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.AllArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CacheAutoConfiguration {
    @Value("${service.cache.prefix}")
    private String cachePrefix;

    @Value("${service.cache.bloom-filter.name}")
    private String bloomFilterName;
    @Value("${service.cache.bloom-filter.expected-insertions}")
    private int expectedInsertions;
    @Value("${service.cache.bloom-filter.false-probability}")
    private double falseProbability;

    @Bean
    public Cache<String, Object> caffeineCache() {
        return (Cache<String, Object>) Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build();
    }

    @Bean
    public LoadingCache<String, Object> caffeineLoadingCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build(key -> null);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public RBloomFilter<String> cachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        cachePenetrationBloomFilter.tryInit(expectedInsertions, falseProbability);
        return cachePenetrationBloomFilter;
    }


}
