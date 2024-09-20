package com.game.lyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Cấu hình RedisTemplate để sử dụng các tính năng nâng cao của Redis
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Sử dụng StringRedisSerializer cho key để key dễ đọc và lưu trữ
        template.setKeySerializer(new StringRedisSerializer());

        // Sử dụng GenericJackson2JsonRedisSerializer để lưu trữ object dưới dạng JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
