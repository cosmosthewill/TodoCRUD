package com.example.TodoCRUD.TaskCRUD.model;

import lombok.Data;

@Data
public class UpdateTaskCommand {
    private final Integer id;
    private final Task task;

    public UpdateTaskCommand(Integer id, Task task) {
        this.id = id;
        this.task = task;
    }
}
