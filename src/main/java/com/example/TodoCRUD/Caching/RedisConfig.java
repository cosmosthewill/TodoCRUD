package com.example.TodoCRUD.Caching;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
//    @Bean// Defines a Spring bean for RedisCacheManager
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        //test
//        //1. Configure ObjectMapper with JavaTimeModule
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.configOverride(LocalDateTime.class)
//                .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss"));
////        objectMapper.activateDefaultTyping(
////                objectMapper.getPolymorphicTypeValidator(),
////                ObjectMapper.DefaultTyping.NON_FINAL,
////                JsonTypeInfo.As.PROPERTY
////        );
//        // 2. Use the new constructor to inject ObjectMapper directly

    /// /        GenericJackson2JsonRedisSerializer genericSerializer =
    /// /                new GenericJackson2JsonRedisSerializer(objectMapper);
//        Jackson2JsonRedisSerializer<TaskDTO> genericSerializer =
//                new Jackson2JsonRedisSerializer<>(objectMapper, TaskDTO.class);
//        //
//        // Define cache configuration
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)) // Set time-to-live (TTL) for cache entries to  minutes
//                .disableCachingNullValues() // Prevent caching of null values
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericSerializer)); // Serialize values using Jackson JSON serializer
//
//        // Create and return a RedisCacheManager with the specified configuration
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfig) // Apply default cache configuration
//                .build();
//    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // JSON Serializer
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // Set Key and HashKey as String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value and HashValue as JSON
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }

}