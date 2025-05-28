package com.example.dto.springapp.service.impl;

import com.example.dto.springapp.dtos.request.EmailDetailsRequest;
import com.example.dto.springapp.service.EmailService;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmailAlert(EmailDetailsRequest emailDetailsRequest) {

    }
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.username}")
//    private String senderEmail;
//
//    @Override
//    public void sendEmailAlert(EmailDetailsDto emailDetailsDto) {
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom(senderEmail);
//            mailMessage.setTo(emailDetailsDto.getRecipient());
//            mailMessage.setSubject(emailDetailsDto.getSubject());
//            mailMessage.setText(emailDetailsDto.getMessageBody());
//
//            javaMailSender.send(mailMessage);
//            System.out.println("mail sent!");
//        } catch (MailException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
