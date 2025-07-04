package com.example.TodoCRUD.Log.Service;

import com.example.TodoCRUD.Log.LogRepository;
import com.example.TodoCRUD.Log.Model.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog (String username, String action, Integer taskId, String details) {
        Log l = new Log();
        l.setUsername(username);
        l.setAction(action);
        l.setTaskId(taskId);
        l.setDetails(details);
        l.setTimestamp(LocalDateTime.now());

        logRepository.save(l);
    }
}
