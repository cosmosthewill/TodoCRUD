package com.example.TodoCRUD.service;

import com.example.TodoCRUD.Command;
import com.example.TodoCRUD.TaskRepository;
import com.example.TodoCRUD.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskService implements Command<Task, Task> {

    private final TaskRepository taskRepository;
    public CreateTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Override
    public ResponseEntity<Task> execute(Task task) {
        Task newTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }
}
