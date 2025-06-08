package com.example.dto.springapp.repository;

import com.example.dto.springapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByAccountNumber(String accountNumber);

    Transaction findByTransactionId(String transId);
}
