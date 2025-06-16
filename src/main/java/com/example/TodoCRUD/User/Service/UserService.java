package com.example.TodoCRUD.User.Service;

import com.example.TodoCRUD.User.Model.UserAccount;
import com.example.TodoCRUD.User.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.TodoCRUD.User.Model.EmailRegex.isValidEmail;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    public ResponseEntity<String> updateUser(String email, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        UserAccount user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + userName));
        //saved
        if (!isValidEmail(email)) throw new RuntimeException("Invalid email format");
        user.setEmail(email);
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User with" + userName + "updated successfully");
    }

    public ResponseEntity<UserAccount> createAccount(UserAccount newUser) {
        if (!isValidEmail(newUser.getEmail())) throw new RuntimeException("Invalid email format");
        if (userRepository.findByUserName(newUser.getUserName()).isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            UserAccount savedUser = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}
