package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.model.Task;
import com.example.TodoCRUD.service.GetTasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private final GetTasksService getTasksService;

    public TaskController(GetTasksService getTasksService) {
        this.getTasksService = getTasksService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return getTasksService.execute(null);
    }
}
