package com.example.TodoCRUD.TaskCRUD.model;

import com.example.TodoCRUD.User.Model.UserAccount;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Table(name = "task")
@ToString(exclude = "sharedWith")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //test
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private UserAccount owner;

    @ManyToMany
    @JoinTable(
            name = "Shared_tasks",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private List<UserAccount> sharedWith;
    //test

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @Column(name = "optional_category")
    private String optional_category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "remind_time")
    private Integer remindTime = 6;

    @Column(name = "remind_again")
    private Boolean remindAgain = true;

}
