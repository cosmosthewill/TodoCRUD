package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniqueCategory {
    private final TaskRepository taskRepository;

    public UniqueCategory(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean validateCategory(String userName ,String category, Integer taskId) {
        List<Task> ownerTasks = taskRepository.findByOwnerUserName(userName);
        if (!ownerTasks.isEmpty()){
            List<String> categories = new ArrayList<>();
            for (Task task : ownerTasks) {
                if (task.getId() != null && !task.getId().equals(taskId)) {
                    String taskCategory = task.getOptional_category();
                    if (taskCategory != null && !taskCategory.isEmpty()) {
                        categories.add(taskCategory);
                    }
                }
            }
            return !categories.contains(category);
        }
        return true;
    }
}
