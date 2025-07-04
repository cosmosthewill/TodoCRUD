package com.example.TodoCRUD.TaskCRUD.controller;

import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.TaskCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.TaskCRUD.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
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

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return getTasksService.execute(null);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Integer id) {
        return getTasksService.getTaskById(id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        return deleteTaskService.execute(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Integer id, @RequestBody Task updateTask) {
        return updateTaskService.execute(new UpdateTaskCommand(id, updateTask));
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO newTask) {
        return createTaskService.execute(newTask);
    }

    //search
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        return searchTaskService.searchTasks(status, category);
    }

    //shared
    @PostMapping("/shared/{id}")
    public ResponseEntity<List<String>> sharedTaskWithUsers(
            @PathVariable Integer id,
            @RequestParam List<String> userNames
    ) {
        return sharedTasksService.addSharedTasks(id, userNames);
    }

    @DeleteMapping("/shared/{id}")
    public ResponseEntity<String> removeSharedTasks(
            @PathVariable Integer id,
            @RequestParam List<String> userNames
    ) {
        return sharedTasksService.removeSharedTasks(id, userNames);
    }
}
