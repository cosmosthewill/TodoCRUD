package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.service.DeleteTaskService;
import com.example.TodoCRUD.model.Task;
import com.example.TodoCRUD.service.GetTasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private final DeleteTaskService deleteTaskService;
    private final GetTasksService getTasksService;
    public TaskController(GetTasksService getTasksService,
                          DeleteTaskService deleteTaskService) {
        this.deleteTaskService = deleteTaskService;
        this.getTasksService = getTasksService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return getTasksService.execute(null);
    }

    @DeleteMapping("delete_task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        return deleteTaskService.execute(id);
    }

}
