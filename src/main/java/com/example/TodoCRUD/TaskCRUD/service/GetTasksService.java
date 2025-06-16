package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskStatus;
import com.example.TodoCRUD.TaskCRUD.model.UpdateTaskCommand;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GetTasksService implements Command <Void, List<Task>>  {

    private final TaskRepository taskRepository;
    private final UpdateTaskService updateTaskService;
    public GetTasksService(TaskRepository taskRepository, UpdateTaskService updateTaskService) {
        this.taskRepository = taskRepository;
        this.updateTaskService = updateTaskService;
    }

    @Override
    public ResponseEntity<List<Task>> execute(Void input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<Task> tasks = taskRepository.findByUserName(userName);
        overdueTasks(tasks);
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(tasks);
        }
    }

    public void overdueTasks(List<Task> tasks){
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(LocalDateTime.now())){
                //debug
                //System.out.println(task.getId());
                //
                task.setStatus(TaskStatus.OVERDUE);
                updateTaskService.execute(new UpdateTaskCommand(task.getId(), task));
            }
        }
    }
}
