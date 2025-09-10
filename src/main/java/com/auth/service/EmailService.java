package com.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: center;">
                    <h2 style="color: #2c3e50;">🔐 Your OTP Verification Code</h2>
                    <p style="font-size: 16px; color: #333;">Use the code below to verify your email address. This code is valid for <strong>10 minutes</strong>.</p>
                    <div style="margin: 20px 0; font-size: 32px; font-weight: bold; color: #e74c3c;">%s</div>
                    <p style="font-size: 14px; color: #888;">If you didn’t request this code, please ignore this email.</p>
                    <hr style="margin: 30px 0;">
                    <p style="font-size: 12px; color: #aaa;">&copy; %d YourAppName. All rights reserved.</p>
                </div>
            </body>
            </html>
        """.formatted(otp, java.time.Year.now().getValue());

            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
