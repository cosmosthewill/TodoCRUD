package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class SearchTaskService {
    private final TaskRepository taskRepository;

    public SearchTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<List<TaskDTO>> searchTasks(String status, String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //System.out.println(userName + "," + status + "," + category);
        List<Task> filteredTask = taskRepository.findByUserName(userName)
                .stream()
                .filter(task ->
                        (status == null || task.getStatus().name().toLowerCase().contains(status.toLowerCase())) &&
                        (category == null || task.getOptional_category().toLowerCase().contains(category.toLowerCase())))
                .toList();
        List<TaskDTO> taskDTOs = filteredTask.stream()
                .map(TaskDTO::new)
                .toList();
        if (taskDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(taskDTOs);
        }
    }
}
