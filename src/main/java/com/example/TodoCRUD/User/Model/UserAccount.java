package com.example.TodoCRUD.User.Model;

import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "account")
@ToString(exclude = "sharedTasks")
public class UserAccount {
    @Id
    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(mappedBy = "sharedWith")
    @JsonIgnore
    private List<Task> sharedTasks;

}
