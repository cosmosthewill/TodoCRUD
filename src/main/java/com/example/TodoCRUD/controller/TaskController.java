package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.model.Task;
import com.example.TodoCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.service.UpdateTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final UpdateTaskService updateTaskService;
    public TaskController(UpdateTaskService updateTaskService) {
        this.updateTaskService = updateTaskService;
    }

    @PutMapping("/update_task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody Task updateTask) {
        return updateTaskService.execute(new UpdateTaskCommand(id, updateTask));
    }
}
