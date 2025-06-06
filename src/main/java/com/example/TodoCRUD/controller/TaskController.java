package com.example.TodoCRUD.controller;

import com.example.TodoCRUD.service.DeleteTaskService;
import com.example.TodoCRUD.model.Task;
import com.example.TodoCRUD.service.GetTasksService;
import com.example.TodoCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.service.UpdateTaskService;
import com.example.TodoCRUD.service.CreateTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    private final DeleteTaskService deleteTaskService;
    private final GetTasksService getTasksService;
    private final UpdateTaskService updateTaskService;
    private final CreateTaskService createTaskService;
    public TaskController(CreateTaskService createTaskService,
                          GetTasksService getTasksService,
                          DeleteTaskService deleteTaskService,
                          UpdateTaskService updateTaskService) {
        this.deleteTaskService = deleteTaskService;
        this.getTasksService = getTasksService;
        this.updateTaskService = updateTaskService;
        this.createTaskService = createTaskService;

    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return getTasksService.execute(null);
    }

    @DeleteMapping("delete_task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id){
        return deleteTaskService.execute(id);
    }

    @PutMapping("/update_task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody Task updateTask) {
        return updateTaskService.execute(new UpdateTaskCommand(id, updateTask));
    }
    
    @PostMapping("/add_task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return createTaskService.execute(task);
    }

}
