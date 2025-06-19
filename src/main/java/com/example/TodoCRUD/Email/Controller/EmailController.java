package com.example.TodoCRUD.Email.Controller;

import com.example.TodoCRUD.Email.Service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String sendTestEmail() {
        emailService.sendEmail("test@example.com", "Test Subject", "This is a test email body.");
        return "Test email sent successfully!";
    }
}
