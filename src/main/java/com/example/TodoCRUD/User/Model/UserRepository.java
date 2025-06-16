package com.example.TodoCRUD.User.Model;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserAccount, String>{
    Optional<UserAccount> findByUserName(String userName);
}
