package com.example.expensenest.service.impl;
import com.example.expensenest.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    JavaMailSender javaMailSender;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public boolean sendVerificationEmail(String recipientEmail, String emailSubject,String emailMessage, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(recipientEmail);
            message.setSubject(emailSubject);
            message.setText(emailMessage+": " + hostname + "/verify?code=" + verificationCode);
            javaMailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }


}