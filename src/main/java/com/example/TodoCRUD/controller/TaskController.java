package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.model.Task;
import com.example.TodoCRUD.service.CreateTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    private final CreateTaskService createTaskService;
    public TaskController(CreateTaskService createTaskService) {
        this.createTaskService = createTaskService;
    }

    @PostMapping("/add_task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return createTaskService.execute(task);
    }
}
