package com.dairystore.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public void sendRegistrationEmailToBuyer(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Успешна регистрация");
        message.setText("Здравей, " + userName + "!\n\nВашата регистрация беше успешна. Добре дошли в нашата система!");
        message.setFrom("ventsislav755@gmail.com");
        mailSender.send(message);
    }

    public void sendRegistrationEmailToSeller(String toEmail, String userName,String onboardingUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Успешна регистрация! Добре дошъл в нашата платформа – потвърди акаунта си в Stripe");
        message.setText("Здравей," + userName + "!\n\nЗа да получаваш плащания, довърши регистрацията си в Stripe тук:\n"
                + onboardingUrl + "\n\nБлагодарим ти!");
        message.setFrom("ventsislav755@gmail.com");
        mailSender.send(message);
    }
}
