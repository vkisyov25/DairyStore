package com.springSecurity.JWT.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{
    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Успешна регистрация");
        message.setText("Здравей, " + userName + "!\n\nВашата регистрация беше успешна. Добре дошли в нашата система!");
        message.setFrom("ventsislav755@gmail.com");
        mailSender.send(message);
    }
}
