package com.example.TodoCRUD.Email.Service;

import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async
    public void sendEmail(String to, String subject , String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("anything@gmail.com");

            mailSender.send(message);
        } catch (MailException e) {
            // Log the error or take action
            System.err.println("Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
