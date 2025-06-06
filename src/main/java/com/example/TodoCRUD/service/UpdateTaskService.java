package com.example.TodoCRUD.service;

import com.example.TodoCRUD.Command;
import com.example.TodoCRUD.TaskRepository;
import com.example.TodoCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateTaskService implements Command<UpdateTaskCommand, Task> {

    private final TaskRepository taskRepository;
    public UpdateTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    @Override
    public ResponseEntity<Task> execute(UpdateTaskCommand taskCommand) {
        Optional<Task> updateTask = taskRepository.findById(taskCommand.getId());
        if(updateTask.isPresent()) {
            Task task = updateTask.get();

            //task.setId(taskCommand.getTask().getId());
            task.setTaskName(taskCommand.getTask().getTaskName());
            task.setTaskDescription(taskCommand.getTask().getTaskDescription());
            task.setIsDone(taskCommand.getTask().getIsDone());

            //Save the updated task back to the repository
            taskRepository.save(task);
            return ResponseEntity.ok(task);
        } else {
            throw new RuntimeException("Task not found with id: " + taskCommand.getId());
        }
    }
}
