package com.dairystore.Services;

public interface EmailService {
    void sendRegistrationEmail(String toEmail, String userName);
}
