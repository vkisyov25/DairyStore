package com.dairystore.Services;

public interface EmailService {
    void sendRegistrationEmailToBuyer(String toEmail, String userName);
    void sendRegistrationEmailToSeller(String toEmail, String userName,String onboardingUrl);
}
