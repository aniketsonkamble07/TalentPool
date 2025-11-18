package com.aniket.placementcell.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeOfficerMail(String email, String name) {
        try {
            logger.info("📧 Preparing welcome email for: {} <{}>", name, email);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Placement Cell - Officer Account Created");
            message.setText(
                    "Dear " + name + ",\n\n" +
                            "Your Placement Officer account has been successfully created!\n\n" +
                            "Account Details:\n" +
                            "- Email: " + email + "\n" +
                            "- Role: Placement Officer\n\n" +
                            "You can now login to the Placement Cell portal to manage student placements, companies, and job opportunities.\n\n" +
                            "Best regards,\n" +
                            "Placement Cell Team"
            );

            // Set from email (important for authentication)
            message.setFrom("noreply@college.edu"); // Use your college email or configured email

            logger.info("📧 Sending email to: {}", email);
            mailSender.send(message);
            logger.info("✅ Email sent successfully to: {}", email);

        } catch (Exception e) {
            logger.error("❌ Failed to send email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Email service error: " + e.getMessage(), e);
        }
    }
}