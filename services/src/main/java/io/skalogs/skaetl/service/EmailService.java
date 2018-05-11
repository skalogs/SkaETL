package io.skalogs.skaetl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void send(String destination, String subject, String message) throws MailException {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("skaetl@skalogs.com");
        msg.setTo(destination);
        msg.setSubject(subject);
        msg.setText(message);

        emailSender.send(msg);
    }
}
