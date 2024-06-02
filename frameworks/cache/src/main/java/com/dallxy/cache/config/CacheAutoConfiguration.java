package com.dallxy.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

@ComponentScan("com.dallxy.cache")
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
    public Cache caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build();
    }

    @Bean
    public LoadingCache<String, Object> caffeineLoadingCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .initialCapacity(100)
                .maximumSize(1000)
                .softValues()
                .build(key -> null);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redissonConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redissonConnectionFactory);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        stringRedisTemplate.setConnectionFactory(redissonConnectionFactory);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return stringRedisTemplate;
    }

    @Bean
    public RBloomFilter<String> cachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        cachePenetrationBloomFilter.tryInit(expectedInsertions, falseProbability);
        return cachePenetrationBloomFilter;
    }


}
