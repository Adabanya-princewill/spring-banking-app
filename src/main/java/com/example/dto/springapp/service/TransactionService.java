package com.example.dto.springapp.service;

import com.example.dto.springapp.dtos.request.TransactionRequest;
import com.example.dto.springapp.dtos.response.TransactionResponse;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionRequest request);

    TransactionResponse getAllTransactions(String accountNumber);

    TransactionResponse getTransaction(String transId, String accountNumber);
}
