package com.example.TodoCRUD.TaskCRUD.controller;

import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.TaskCRUD.service.*;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.User.Model.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final DeleteTaskService deleteTaskService;
    private final GetTasksService getTasksService;
    private final UpdateTaskService updateTaskService;
    private final CreateTaskService createTaskService;
    private final SearchTaskService searchTaskService;
    private final SharedTasksService sharedTasksService;
    public TaskController(CreateTaskService createTaskService,
                          GetTasksService getTasksService,
                          DeleteTaskService deleteTaskService,
                          UpdateTaskService updateTaskService,
                          SearchTaskService searchTaskService,
                          SharedTasksService sharedTasksService
    ) {
        this.deleteTaskService = deleteTaskService;
        this.getTasksService = getTasksService;
        this.updateTaskService = updateTaskService;
        this.createTaskService = createTaskService;
        this.searchTaskService = searchTaskService;
        this.sharedTasksService = sharedTasksService;
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
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO newTask) {
        return createTaskService.execute(newTask);
    }

    //search
    @GetMapping("/search_tasks")
    public ResponseEntity<List<TaskDTO>> searchTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        return searchTaskService.searchTasks(status, category);
    }

    //shared
    @PostMapping("/shared_task/{id}")
    public ResponseEntity<List<String>> sharedTaskWithUsers(
            @PathVariable Integer id,
            @RequestParam List<String> userNames
    ) {
        return sharedTasksService.addSharedTasks(id, userNames);
    }

}
