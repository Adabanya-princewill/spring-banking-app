package com.example.dto.springapp.service.impl;

import com.example.dto.springapp.dtos.request.TransactionRequest;
import com.example.dto.springapp.dtos.response.TransResponse;
import com.example.dto.springapp.dtos.response.TransactionResponse;
import com.example.dto.springapp.model.Transaction;
import com.example.dto.springapp.model.User;
import com.example.dto.springapp.repository.TransactionRepository;
import com.example.dto.springapp.repository.UserRepository;
import com.example.dto.springapp.service.TransactionService;
import com.example.dto.springapp.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveTransaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .accountNumber(request.getAccountNumber())
                .status("SUCCESS")
                .amount(request.getAmount())
                .build();
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionResponse getAllTransactions(String accountNumber) {

        log.info(AccountUtils.getLoggedInUser());
        if (accountNumber.length() != 10) {
            return TransactionResponse.builder()
                    .responseCode("00")
                    .responseMessage("invalid account number")
                    .transResponse(null)
                    .build();
        }
        User user = userRepository.findByEmail(AccountUtils.getLoggedInUser());
        log.info("user : {}", user);
        int userAccountNumber = Integer.parseInt(user.getAccountNumber());

        if (!Objects.equals(userAccountNumber, Integer.parseInt(accountNumber))) {
            return TransactionResponse.builder()
                    .responseCode("00")
                    .responseMessage("account number does not belong to user")
                    .transResponse(null)
                    .build();
        }

        List<Transaction> transactions = transactionRepository.findAllByAccountNumber(accountNumber);

        List<TransResponse> response = new ArrayList<>();

        for (Transaction transaction : transactions) {
            TransResponse res = TransResponse.builder()
                    .transactionId(transaction.getTransactionId())
                    .amount(transaction.getAmount())
                    .status(transaction.getStatus())
                    .transactionType(transaction.getTransactionType())
                    .timeStamp(transaction.getTimestamp())
                    .build();
            response.add(res);
        }
        return TransactionResponse.builder()
                .responseCode("001")
                .responseMessage("transactions list gotten successfully")
                .transResponse(response)
                .build();
    }

    @Override
    public TransactionResponse getTransaction(String transId, String accountNumber) {
        if (accountNumber.length() != 10) {
            return TransactionResponse.builder()
                    .responseCode("00")
                    .responseMessage("invalid account number")
                    .transResponse(null)
                    .build();
        }

        Transaction transaction = transactionRepository.findByTransactionId(transId);

        if (transaction == null) {
            return
                    TransactionResponse.builder()
                            .responseCode("00")
                            .responseMessage("invalid transaction id")
                            .transResponse(null)
                            .build();
        }

        String loggedInUser = AccountUtils.getLoggedInUser();

        User user = userRepository.findByEmail(loggedInUser);

        boolean isValid = user.getAccountNumber().equals(accountNumber);

        if (isValid && (transaction.getAccountNumber().equals(user.getAccountNumber()))) {
            return TransactionResponse.builder()
                    .responseCode("002")
                    .responseMessage("success")
                    .transResponse(List.of(
                            TransResponse.builder()
                                    .transactionId(transaction.getTransactionId())
                                    .amount(transaction.getAmount())
                                    .status(transaction.getStatus())
                                    .transactionType(transaction.getTransactionType())
                                    .timeStamp(transaction.getTimestamp())
                                    .build())
                    )
                    .build();
        }

        return TransactionResponse.builder()
                .responseCode("00")
                .responseMessage("invalid account number")
                .transResponse(null)
                .build();
    }
}
