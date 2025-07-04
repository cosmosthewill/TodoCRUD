package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeleteTaskService implements Command<Integer, String> {

    private final TaskRepository taskRepository;
    private final TaskCacheService taskCacheService;

    public DeleteTaskService(TaskRepository taskRepository, TaskCacheService taskCacheService) {
        this.taskRepository = taskRepository;
        this.taskCacheService = taskCacheService;
    }

    @Override
    public ResponseEntity<String> execute(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<Task> userTasks = taskRepository.findByOwnerUserName(userName);
        if (userTasks.isEmpty()) {
            throw new ApiException("No tasks found", 404);
        }

        Optional<Task> deletedTask = userTasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();

        if (deletedTask.isPresent()) {
            taskRepository.deleteById(id);
            // Optionally, you can also clear the cache for this task if caching is implemented
            taskCacheService.deleteCachedTask(userName, id);
            return ResponseEntity.ok("Task with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Task with ID " + id + " not found.");
        }
    }
}
