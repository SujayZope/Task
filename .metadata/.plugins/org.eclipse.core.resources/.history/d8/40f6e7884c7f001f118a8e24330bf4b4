package com.example.sms.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.sms.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);

	
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetLink) {
        String subject = "Password Reset Request";
        String text = "Dear user,\n\n"
                + "You have requested to reset your password. Please click the link below to reset your password:\n"
                + resetLink + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Regards,\n"
                + "SMS Team";

        sendEmail(email, subject, text);
    }
}