package com.example.dto.springapp.service.impl;

import com.example.dto.springapp.dtos.request.EmailDetailsRequest;
import com.example.dto.springapp.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDetailsRequest emailDetailsDto) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetailsDto.getRecipient());
            mailMessage.setSubject(emailDetailsDto.getSubject());
            mailMessage.setText(emailDetailsDto.getMessageBody());

           // javaMailSender.send(mailMessage);
            System.out.println("mail sent!");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailAlertWithAttachment(EmailDetailsRequest emailDetailsDto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailDetailsDto.getRecipient());
            mimeMessageHelper.setSubject(emailDetailsDto.getSubject());
            mimeMessageHelper.setText(emailDetailsDto.getMessageBody());

            FileSystemResource file = new FileSystemResource(new File(emailDetailsDto.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            System.out.println("mail sent!");
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
