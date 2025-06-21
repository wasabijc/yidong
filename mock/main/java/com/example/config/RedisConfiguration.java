package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfiguration 类配置 RedisTemplate，
 * 并为键和值设置自定义的序列化策略。
 */
@Configuration("RedisConfig")
public class RedisConfiguration<V> {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    /**
     * 自定义一个 RedisTemplate bean，使用提供的 RedisConnectionFactory 创建。
     * 目的是指定序列化器，而不使用默认的JDK序列化器
     *
     * @param factory Redis 连接工厂
     * @return 配置好的 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {

        logger.info("正在配置自定义的redisTemplate类，使用string序列化键，json序列化值");

        RedisTemplate<String, V> template = new RedisTemplate<>();

        // 设置 Redis 连接工厂
        template.setConnectionFactory(factory);

        // 设置键的序列化器为字符串序列化器
        template.setKeySerializer(RedisSerializer.string());

        // 设置值的序列化器为 JSON 序列化器
        template.setValueSerializer(RedisSerializer.json());

        // 设置哈希键的序列化器为字符串序列化器
        template.setHashKeySerializer(RedisSerializer.string());

        // 设置哈希值的序列化器为 JSON 序列化器
        template.setHashValueSerializer(RedisSerializer.json());

        // 在属性设置完成后进行初始化
        template.afterPropertiesSet();

        return template;
    }

}
