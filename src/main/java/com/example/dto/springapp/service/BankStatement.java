package com.example.dto.springapp.service;

import com.example.dto.springapp.dtos.response.TransactionResponse;
import com.example.dto.springapp.model.Transaction;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public interface BankStatement {
    TransactionResponse generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException;

}
