package com.example.TodoCRUD.Constants;

public class AppConstants {
    public static final int UPDATE_REMIND_MAIL_TIMEOUT = 60 * 60 * 1000; // 1 hour
    public static final int UPDATE_OVERDUE_TASK_TIMEOUT = 10 * 60 * 1000; // 10 minutes
    public static final int TTL_REDIS_CACHE_IN_MINUTES = 10; //10mins
    public static String DEFAULT_REDIS_HOST;
    public static int DEFAULT_REDIS_PORT;
}
