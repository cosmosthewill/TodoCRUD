package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.User.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskService implements Command<TaskDTO, Task> {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCacheService taskCacheService;
    @Autowired
    private UniqueCategory uniqueCategory;

    public CreateTaskService(TaskRepository taskRepository, UserRepository userRepository, TaskCacheService taskCacheService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCacheService = taskCacheService;
    }


    @Override
    public ResponseEntity<Task> execute(TaskDTO newTask) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Task savedTask = ConvertTask(userName, newTask);
        if (uniqueCategory.validateCategory(userName, savedTask.getOptional_category(), newTask.getId())) {
            taskRepository.save(savedTask);
            //cache the task
            taskCacheService.updateCachedTask(newTask, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
        }
        throw new ApiException("Category " + newTask.getOptional_category() + "already existed", HttpStatus.BAD_REQUEST.value());
    }

    private Task ConvertTask(String username, TaskDTO newTask) {
        Task savedTask = new Task();
        savedTask.setOwner(userRepository.findByUserName(username).get());

        savedTask.setTitle(newTask.getTitle());
        savedTask.setDescription(newTask.getDescription());
        savedTask.setDeadline(newTask.getDeadline());
        savedTask.setOptional_category(newTask.getOptional_category());
        savedTask.setStatus(newTask.getStatus());
        savedTask.setRemindTime(newTask.getRemindTime());
        savedTask.setRemindAgain(newTask.getRemindAgain());

        return savedTask;
    }
}
