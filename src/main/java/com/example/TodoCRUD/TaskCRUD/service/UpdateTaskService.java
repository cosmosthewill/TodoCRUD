package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import com.example.TodoCRUD.TaskCRUD.model.TaskStatus;
import com.example.TodoCRUD.TaskCRUD.model.UpdateTaskCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateTaskService implements Command<UpdateTaskCommand, TaskDTO> {

    private final TaskRepository taskRepository;
    private final TaskCacheService taskCacheService;
    @Autowired
    private UniqueCategory uniqueCategory;

    public UpdateTaskService(TaskRepository taskRepository, TaskCacheService taskCacheService) {
        this.taskRepository = taskRepository;
        this.taskCacheService = taskCacheService;
    }

    public void setTaskCompleted(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
    }

    public void UpdateTask(Task task, UpdateTaskCommand taskCommand, String userName) {
        Task incomingTask = taskCommand.getTask();

        // title
        if (incomingTask.getTitle() != null) {
            task.setTitle(incomingTask.getTitle());
        }

        // description
        if (incomingTask.getDescription() != null) {
            task.setDescription(incomingTask.getDescription());
        }
        // status

        if (incomingTask.getStatus() != null) {
            task.setStatus(incomingTask.getStatus());
        }

        // deadline
        if (incomingTask.getDeadline() != null) {
            task.setDeadline(incomingTask.getDeadline());
        }

        // category only owner can set or update category
        if (incomingTask.getOptional_category() != null) {
            if (uniqueCategory.validateCategory(userName, incomingTask.getOptional_category(), taskCommand.getId())) {
                task.setOptional_category(incomingTask.getOptional_category());
            } else {
                throw new RuntimeException("Category already exists for user: " + userName);
            }
        }
        //remind time
        if (incomingTask.getRemindTime() != null) {
            task.setRemindTime(incomingTask.getRemindTime());
        }
        //remind again
        if (incomingTask.getRemindAgain() != null) {
            task.setRemindAgain(incomingTask.getRemindAgain());
        }

    }

    @Override
    public ResponseEntity<TaskDTO> execute(UpdateTaskCommand taskCommand) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<Task> userTasks = taskRepository.findByUserName(userName);
        if (userTasks.isEmpty()) {
            throw new ApiException("No tasks found for user: " + userName, 404);
        }
        Optional<Task> updateTask = userTasks.stream()
                .filter(task -> task.getId().equals(taskCommand.getId()))
                .findFirst();

        if (updateTask.isPresent()) {
            Task task = updateTask.get();
            UpdateTask(task, taskCommand, userName);
            taskRepository.save(task);
            //cached
            taskCacheService.updateCachedTask(new TaskDTO(task), userName);
            return ResponseEntity.ok(new TaskDTO(task));
        } else {
            throw new ApiException("Task with id" + taskCommand.getId() + "not found", 404);
        }
    }
}
