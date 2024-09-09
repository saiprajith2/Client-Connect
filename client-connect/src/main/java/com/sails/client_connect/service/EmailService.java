package com.sails.client_connect.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * @param fromEmail
     * @param toEmail
     * @param subject
     * @param body      Creating the object in which we load the parameters required for an email
     *                  Send that object using the mail sender
     * @throws MessagingException
     */
    public void sendDynamicEmail(String fromEmail, String toEmail, String subject, String body) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Sent...");
    }

    /**
     * @param toEmail
     * @param otp     Creating the object to load the details for the otp
     *                Send the object using mail sender
     */
    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
        System.out.println("Otp Sent...");
    }
}
