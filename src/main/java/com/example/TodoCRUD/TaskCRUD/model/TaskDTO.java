package com.example.TodoCRUD.TaskCRUD.model;

import com.example.TodoCRUD.User.Model.UserAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskDTO {
    private Integer id;
    private String owner;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    private String optional_category;
    private TaskStatus status;
    private List<String> sharedWith;
    private Integer remindTime;
    private Boolean remindAgain;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.optional_category = task.getOptional_category();
        this.status = task.getStatus();
        this.sharedWith = task.getSharedWith().stream()
                .map(UserAccount::getUserName)
                .collect(Collectors.toList());
        this.remindTime = task.getRemindTime();
        this.remindAgain = task.getRemindAgain();
        this.owner = task.getOwner().getUserName();
    }

    public TaskDTO() {
        // Default constructor
    }
}
