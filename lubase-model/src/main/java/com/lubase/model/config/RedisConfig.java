package com.lubase.model.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
public class RedisConfig {

//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Bean
//    public RedisTemplate redisTemplateInit() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
//        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
//        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//        if (redisProperties.getTimeToLive() != null) {
//            configuration = configuration.entryTtl(redisProperties.getTimeToLive());
//        }
//        if (redisProperties.getKeyPrefix() != null) {
//            configuration = configuration.prefixCacheNameWith(redisProperties.getKeyPrefix());
//        }
//        if (!redisProperties.isCacheNullValues()) {
//            configuration.disableCachingNullValues();
//        }
//        if (!redisProperties.isUseKeyPrefix()) {
//            configuration = configuration.disableKeyPrefix();
//        }
//        return configuration;
//    }
}
