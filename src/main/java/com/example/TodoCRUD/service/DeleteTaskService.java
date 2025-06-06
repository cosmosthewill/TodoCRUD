package com.example.TodoCRUD.service;

import com.example.TodoCRUD.Command;
import com.example.TodoCRUD.TaskRepository;
import com.example.TodoCRUD.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteTaskService implements Command<Integer, String> {

    private final TaskRepository taskRepository;
    public DeleteTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ResponseEntity<String> execute(Integer id) {
        Optional<Task> deletedTask = taskRepository.findById(id);
        if(deletedTask.isPresent()){
            taskRepository.deleteById(id);
            return ResponseEntity.ok("Task with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Task with ID " + id + " not found.");
        }
    }
}
