package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.Command;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.TaskStatus;
import com.example.TodoCRUD.TaskCRUD.model.UpdateTaskCommand;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UpdateTaskService implements Command<UpdateTaskCommand, Task> {

    private final TaskRepository taskRepository;
    public UpdateTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setTaskCompleted(Integer id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);
    }

    @Override
    public ResponseEntity<Task> execute(UpdateTaskCommand taskCommand) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        List<Task> userTasks = taskRepository.findByUserName(userName);
        if(userTasks.isEmpty()) {
            throw new RuntimeException("No tasks found for user: " + userName);
        }
        Optional<Task> updateTask = userTasks.stream()
                .filter(task -> task.getId().equals(taskCommand.getId()))
                .findFirst();

        if(updateTask.isPresent()) {
            Task task = updateTask.get();

            //task.setId(taskCommand.getTask().getId());
            Task incomingTask = taskCommand.getTask();

            if (incomingTask.getTitle() != null) {
                task.setTitle(incomingTask.getTitle());
            }

            if (incomingTask.getDescription() != null) {
                task.setDescription(incomingTask.getDescription());
            }

            if (incomingTask.getStatus() != null) {
                task.setStatus(incomingTask.getStatus());
            }

            if (incomingTask.getDeadline() != null) {
                task.setDeadline(incomingTask.getDeadline());
            }

            if (incomingTask.getOptional_category() != null) {
                task.setOptional_category(incomingTask.getOptional_category());
            }

            //Save the updated task back to the repository
            taskRepository.save(task);
            return ResponseEntity.ok(task);
        } else {
            throw new RuntimeException("Task not found with id: " + taskCommand.getId());
        }
    }
}
