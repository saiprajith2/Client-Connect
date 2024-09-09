package com.sails.client_connect.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    /**
     * Tests the sendDynamicEmail method of EmailService.
     * Verifies that the mailSender.send method is called with the correct SimpleMailMessage object.
     *
     * @throws MessagingException if there is an issue with sending the email
     */
    @Test
    void testSendDynamicEmail() throws MessagingException {
        // Arrange
        String fromEmail = "test@example.com";
        String toEmail = "recipient@example.com";
        String subject = "Test Subject";
        String body = "This is a test email.";

        // Act
        emailService.sendDynamicEmail(fromEmail, toEmail, subject, body);

        // Assert
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        // Verify that mailSender.send was called with the expected message
        verify(mailSender).send(eq(message));
    }

    @Test
    void testSendOtp() {
        // Arrange
        String toEmail = "recipient@example.com";
        String otp = "123456";

        // Act
        emailService.sendOtp(toEmail, otp);

        // Assert
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        // Verify that mailSender.send was called with the expected message
        verify(mailSender).send(eq(message));
    }
}
