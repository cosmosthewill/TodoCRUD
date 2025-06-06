package com.example.TodoCRUD.service;

import com.example.TodoCRUD.Command;
import com.example.TodoCRUD.TaskRepository;
import com.example.TodoCRUD.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetTasksService implements Command <Void, List<Task>>  {

    private final TaskRepository taskRepository;
    public GetTasksService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<List<Task>> execute(Void input) {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(tasks);
        }
    }
}
