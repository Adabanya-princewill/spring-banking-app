package com.example.dto.springapp.service;

import com.example.dto.springapp.dtos.request.CreditDebitRequest;
import com.example.dto.springapp.dtos.request.EnquiryRequest;
import com.example.dto.springapp.dtos.request.TransferRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.dtos.response.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    BankResponse getAccount(long id);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse TransferToAccount(TransferRequest request);
}
