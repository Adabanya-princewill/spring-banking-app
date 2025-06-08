package com.example.dto.springapp.service;


import com.example.dto.springapp.dtos.request.EmailDetailsRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmailAlert(EmailDetailsRequest emailDetailsRequest);
    void sendEmailAlertWithAttachment(EmailDetailsRequest emailDetailsDto);
}
