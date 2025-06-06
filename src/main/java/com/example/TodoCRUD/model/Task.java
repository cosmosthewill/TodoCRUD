package com.example.TodoCRUD.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "taskname")
    private String taskName;

    @Column(name = "taskdescription")
    private String taskDescription;

    @Column(name = "isdone")
    private Boolean isDone;
}
