package com.vk.recharge.service;

import com.vk.recharge.entity.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationSuccessEmail(String toEmail, String phoneNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Successful");
        message.setText(createEmailBody(phoneNumber));
        javaMailSender.send(message);
    }

    private String createEmailBody(String phoneNumber) {
        return "Dear User,\n\n" +
                "Congratulations! Your registration was successful.\n\n" +
                "Phone Number: " + phoneNumber + "\n\n" +
                "Thank you for registering with us.\n\n" +
                "Best Regards,\n" +
                "The DotLink Team";
    }

    public void sendRechargeSuccessEmail(String to, Plan plan, LocalDate validityEndDate) {
        String subject = "Recharge Successful";
        String emailBody = "Dear User,\n\nYour recharge of the " + plan.getName() + " plan was successful.\n" +
                "Plan Details:\nPrice: ₹" + plan.getPrice() + "\nValidity: " + plan.getValidity() + " days\n" +
                "Your recharge is valid until: " + validityEndDate + "\n\nThank you for choosing us!";

        sendSimpleMessage(to, subject, emailBody);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

}
