package com.example.TodoCRUD.Constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class AppPropertiesInitializer implements ApplicationRunner {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AppConstants.DEFAULT_REDIS_HOST = redisHost;
        AppConstants.DEFAULT_REDIS_PORT = redisPort;
    }
}
