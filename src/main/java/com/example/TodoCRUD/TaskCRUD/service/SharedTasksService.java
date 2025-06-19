package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.User.Model.UserAccount;
import com.example.TodoCRUD.User.Model.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SharedTasksService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public SharedTasksService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<String>> addSharedTasks(Integer taskId, List<String> userNames) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ownerName = authentication.getName();

        Task sharedTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        if(!Objects.equals(sharedTask.getOwner().getUserName(), ownerName)) throw new RuntimeException("You are not the owner of this task.");
        for (String userName : userNames) {
            UserAccount user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + userName));
            sharedTask.getSharedWith().add(user);
        }
        taskRepository.save(sharedTask);
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
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        
        if(!Objects.equals(sharedTask.getOwner().getUserName(), ownerName)) throw new RuntimeException("You are not the owner of this task.");
        List<UserAccount> usersToRemove = new ArrayList<>();
        for (String userName : userNames) {
            UserAccount user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + userName));
            usersToRemove.add(user);
        }
        sharedTask.getSharedWith().removeAll(usersToRemove);
        taskRepository.save(sharedTask);
        return ResponseEntity.ok("Users removed from shared task successfully.");
    }
}
