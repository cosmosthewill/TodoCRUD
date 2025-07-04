package com.example.TodoCRUD.User.Controller;

import com.example.TodoCRUD.User.Model.UserAccount;
import com.example.TodoCRUD.User.Model.UserDTO;
import com.example.TodoCRUD.User.Model.UserRepository;
import com.example.TodoCRUD.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserAccount> register(@RequestBody UserAccount newUser) {
        return userService.createAccount(newUser);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userUpdate) {
        return userService.updateUser(userUpdate.getEmail(), userUpdate.getPassword());
    }

}
