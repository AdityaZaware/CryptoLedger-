package com.ben.StockTrading.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationOtpEmailForgotPassword(String email, String otp, Long userId) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String subject = "Verification OTP";

        String text = "Your verification OTP is: " + otp;


        helper.setTo(email);
        helper.setText(text, true);
        helper.setSubject(subject);

        try {
            javaMailSender.send(mimeMessage);
        }
        catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String subject = "Verification OTP";

        String text = "Your verification OTP is: " + otp;

        helper.setTo(email);
        helper.setText(text, true);
        helper.setSubject(subject);

        try {
            javaMailSender.send(mimeMessage);
        }
        catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
