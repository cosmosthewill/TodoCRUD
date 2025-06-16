package com.example.TodoCRUD.User.Model;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
