package com.example.TodoCRUD.TaskCRUD;

import com.example.TodoCRUD.TaskCRUD.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN t.sharedWith u WHERE t.owner.userName = :userName OR u.userName = :userName")
    List<Task> findByUserName (String userName);

    @Query("SELECT t FROM Task t WHERE t.owner.userName = :userName")
    List<Task> findByOwnerUserName(String userName);

    @Query("SELECT t FROM Task t WHERE t.status LIKE %:status% OR t.optional_category LIKE %:category%")
    List<Task> findByStatusOrCategory(String status, String category);
}
