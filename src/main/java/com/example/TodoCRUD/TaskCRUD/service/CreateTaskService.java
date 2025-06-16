package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.User.Model.UserAccount;
import com.example.TodoCRUD.User.Model.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskService implements Command<TaskDTO, Task> {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public CreateTaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Override
    public ResponseEntity<Task> execute(TaskDTO newTask) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Task savedTask = ConvertTask(userName, newTask);
        taskRepository.save(savedTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    private Task ConvertTask(String username, TaskDTO newTask) {
        Task savedTask = new Task();
        savedTask.setOwner(userRepository.findByUserName(username).get());

        savedTask.setTitle(newTask.getTitle());
        savedTask.setDescription(newTask.getDescription());
        savedTask.setDeadline(newTask.getDeadline());
        savedTask.setOptional_category(newTask.getOptional_category());
        savedTask.setStatus(newTask.getStatus());
        return savedTask;
    }
}
