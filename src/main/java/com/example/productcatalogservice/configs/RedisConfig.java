package com.example.productcatalogservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> object = new RedisTemplate<>();
        object.setConnectionFactory(redisConnectionFactory);
        return object;
    }

    @Bean
    RedisTemplate<String, List<Object>> redisTemplateObjects(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Object>> objects = new RedisTemplate<>();
        objects.setConnectionFactory(redisConnectionFactory);
        return objects;
    }


}
