package com.ainkai.emailservice;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class OrderConfirmationEmail {


    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to,String subject,String text) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("from@demomailtrap.com");
        helper.setTo("work.mitakshar@gmail.com");
        helper.setSubject(subject);
        helper.setText(text);
        helper.setSentDate(Date.from(Instant.now()));
        mailSender.send(message);

    }




}
