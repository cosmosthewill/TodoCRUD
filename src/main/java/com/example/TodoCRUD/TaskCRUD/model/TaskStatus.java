package com.example.TodoCRUD.TaskCRUD.model;

import lombok.Getter;

@Getter
public enum TaskStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    OVERDUE("Overdue");
    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

}
