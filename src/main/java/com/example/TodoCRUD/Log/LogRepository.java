package com.example.TodoCRUD.Log;

import com.example.TodoCRUD.Log.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

}
