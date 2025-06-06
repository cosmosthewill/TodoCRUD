package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.service.DeleteTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final DeleteTaskService deleteTaskService;
    public TaskController(DeleteTaskService deleteTaskService) {
        this.deleteTaskService = deleteTaskService;
    }

    @DeleteMapping("delete_task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        return deleteTaskService.execute(id);
    }
}
