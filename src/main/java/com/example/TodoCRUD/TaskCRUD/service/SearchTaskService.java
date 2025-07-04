package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchTaskService {
    private final TaskCacheService taskCacheService;

    public SearchTaskService(TaskCacheService taskCacheService) {
        this.taskCacheService = taskCacheService;
    }

    public ResponseEntity<List<TaskDTO>> searchTasks(String status, String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<TaskDTO> cachedTasks = taskCacheService.searchTasksByCategoryOrDescription(userName, category, status);
        if (cachedTasks.isEmpty()) {
            throw new ApiException("No tasks found", 404);
        }
        return ResponseEntity.ok(cachedTasks);
    }
}
