package com.example.TodoCRUD.Caching;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisJsonService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisJsonService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void saveAsJson(String key, T object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write JSON to Redis", e);
        }
    }

    public <T> T getFromJson(String key, Class<T> type) {
        try {
            String json = redisTemplate.opsForValue().get(key).toString();
            if (json == null) return null;
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON from Redis", e);
        }
    }
}
