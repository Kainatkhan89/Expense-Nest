package com.example.expensenest.service;

public interface EmailSenderService {
    public boolean sendVerificationEmail(String recipientEmail, String emailSubject,String emailMessage,String verificationCode);
}
