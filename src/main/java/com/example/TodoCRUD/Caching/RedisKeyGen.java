package com.example.TodoCRUD.Caching;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyGen {
    private final static String TASK_CACHE_PREFIX = "TASK_CACHE: ";

    public static String taskKey(String username, String taskId) {
        return TASK_CACHE_PREFIX + username + ":task:" + taskId;
    }

    public static String taskListSetKey(String username) {
        return TASK_CACHE_PREFIX + username + ":task-list-ids";
    }

    public static String taskLoadCompletedUser(String username) {
        return TASK_CACHE_PREFIX + username + ":COMPLETED";
    }
}
