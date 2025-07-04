package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.ExceptionHandler.ApiException;
import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTasksService implements Command<Void, List<TaskDTO>> {

    private final TaskRepository taskRepository;
    private final TaskCacheService taskCacheService;

    public GetTasksService(TaskRepository taskRepository, TaskCacheService taskCacheService) {
        this.taskRepository = taskRepository;
        this.taskCacheService = taskCacheService;
    }


    @Override
    public ResponseEntity<List<TaskDTO>> execute(Void input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<TaskDTO> tasks = taskCacheService.getCachedTasksForUser(userName);
        if (tasks == null || tasks.isEmpty()) {
            List<Task> userTasks = taskRepository.findByUserName(userName);

            if (userTasks.isEmpty()) {
                throw new ApiException("No tasks found for user: " + userName, 404);
            } else {
                List<TaskDTO> taskDTOs = userTasks.stream()
                        .map(TaskDTO::new)
                        .toList();
                // Cache the tasks for the user
                for (TaskDTO taskDTO : taskDTOs) {
                    taskCacheService.updateCachedTask(taskDTO, userName);
                }
                taskCacheService.setCompleteLoadAllTasksForUser(userName);
                return ResponseEntity.ok(taskDTOs);
            }
        } else {
            return ResponseEntity.ok(tasks);
        }
    }


    public ResponseEntity<TaskDTO> getTaskById(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        TaskDTO returnedTask = taskCacheService.getCachedTaskById(id, userName);
        if (returnedTask == null) {
            Task task = taskRepository.findById(id).orElse(null);
            if (task == null) {
                throw new ApiException("No task found for id: " + id, 404);
            } else {
                returnedTask = new TaskDTO(task);
                // Optionally cache the task if not found in cache
                taskCacheService.updateCachedTask(returnedTask, userName);
            }
        }
        return ResponseEntity.ok(returnedTask);
    }
}
