package com.example.TodoCRUD.TaskCRUD.service;

import com.example.TodoCRUD.Constants.AppConstants;
import com.example.TodoCRUD.Email.Service.EmailService;
import com.example.TodoCRUD.TaskCRUD.TaskRepository;
import com.example.TodoCRUD.TaskCRUD.model.Task;
import com.example.TodoCRUD.TaskCRUD.model.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.logging.Logger;
@Slf4j
@Controller
public class TaskScheduleService {
    private final EmailService emailService;
    private final TaskRepository taskRepository;

    public TaskScheduleService(EmailService emailService, TaskRepository taskRepository) {
        this.emailService = emailService;
        this.taskRepository = taskRepository;
    }

    //Update
//    @Scheduled (fixedRate = AppConstants.UPDATE_REMIND_MAIL_TIMEOUT)
//    private void checkAndSendReminders() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Task> reminderTasks = taskRepository.findAll();
//        reminderTasks.removeIf(task -> task.getStatus() == TaskStatus.COMPLETED
//                || task.getStatus() == TaskStatus.OVERDUE
//                || task.getDeadline().isAfter(now.plusHours(task.getRemindTime()))
//                || task.getRemindAgain() == false);
//
//        for(Task t : reminderTasks) {
//            //testing
//            System.out.println("Sending reminder for task: " + t.getId());
//            System.out.println("deadline: " + t.getDeadline() + " timenow: " + now + " " + now.plusHours(t.getRemindTime()));
//            //testing end
//            emailService.sendEmail(t.getOwner().getEmail(),
//                    "Reminder for Task: " + t.getTitle(),
//                    "This is a reminder for your task: " + t.getTitle() +
//                            "\nDescription: " + t.getDescription() +
//                            "\nDue Date: " + t.getDeadline() +
//                            "\nPlease complete it before the deadline.");
//
//        }
//    }
    //update overdue tasks
    @Scheduled(fixedRate = AppConstants.UPDATE_OVERDUE_TASK_TIMEOUT)
    public void updateOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (task.getDeadline().isBefore(now) && task.getStatus()
                    != TaskStatus.COMPLETED
                    && task.getStatus() != TaskStatus.OVERDUE) {
                System.out.println("overdue " + task.getId() + " " + task.getDeadline() + " " + now);
                task.setStatus(TaskStatus.OVERDUE);
                taskRepository.save(task);
            }
            else if (task.getDeadline().isAfter(now) && task.getStatus() == TaskStatus.OVERDUE) {
                System.out.println("pending " + task.getId() + " " + task.getDeadline() + " " + now);
                log.info("pending task {}: {}", task.getId(), task.getDeadline());
                task.setStatus(TaskStatus.PENDING);
                taskRepository.save(task);
            }
        }
    }
}
