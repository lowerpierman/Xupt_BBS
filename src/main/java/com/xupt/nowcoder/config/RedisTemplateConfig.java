package com.xupt.nowcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author yzw
 * @Date 2022-07-13 17:18 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //序列化java中的数据结构 方便传到redis
        //序列化Key
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //序列化Value
        redisTemplate.setValueSerializer(RedisSerializer.json());
        //序列化Hash的Key
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //序列化Hash中的Value
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        //使其生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
