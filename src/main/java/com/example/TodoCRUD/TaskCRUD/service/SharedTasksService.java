package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.User.Model.UserAccount;
import com.example.TodoCRUD.User.Model.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SharedTasksService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCacheService taskCacheService;

    public SharedTasksService(TaskRepository taskRepository, UserRepository userRepository, TaskCacheService taskCacheService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCacheService = taskCacheService;
    }

    public ResponseEntity<List<String>> addSharedTasks(Integer taskId, List<String> userNames) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerName = authentication.getName();

        Task sharedTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApiException("Task not found with ID: " + taskId, 404));
        if (!Objects.equals(sharedTask.getOwner().getUserName(), ownerName))
            throw new RuntimeException("You are not the owner of this task.");
        for (String userName : userNames) {
            UserAccount user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new ApiException("User not found with username: " + userName, 404));
            sharedTask.getSharedWith().add(user);
            if (sharedTask.getSharedWith().contains(user)) {
                throw new ApiException("User " + userName + " is alreadt shared with this task.", 400);
            }
        }
        taskRepository.save(sharedTask);
        //cached task
        taskCacheService.updateCachedTask(new TaskDTO(sharedTask), ownerName);
        List<String> sharedUserNames = sharedTask.getSharedWith()
                .stream()
                .map(UserAccount::getUserName)
                .toList();
        return ResponseEntity.ok(sharedUserNames);
    }

    public ResponseEntity<String> removeSharedTasks(Integer taskId, List<String> userNames) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerName = authentication.getName();

        Task sharedTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApiException("Task not found with ID: " + taskId, 404));

        if (!Objects.equals(sharedTask.getOwner().getUserName(), ownerName))
            throw new ApiException("You are not the owner of this task.", 403);
        List<UserAccount> usersToRemove = new ArrayList<>();
        for (String userName : userNames) {
            UserAccount user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new ApiException("User not found with username: " + userName, 404));
            usersToRemove.add(user);
        }
        for (UserAccount user : usersToRemove) {
            if (!sharedTask.getSharedWith().contains(user)) {
                throw new ApiException("User " + user.getUserName() + " is not shared with this task.", 400);
            } else {
                sharedTask.getSharedWith().remove(user);
            }
        }
        taskRepository.save(sharedTask);
        //cached
        taskCacheService.updateCachedTask(new TaskDTO(sharedTask), ownerName);
        return ResponseEntity.ok("Users removed from shared task successfully.");
    }
}
