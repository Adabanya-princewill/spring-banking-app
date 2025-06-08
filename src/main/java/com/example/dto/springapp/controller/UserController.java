package com.example.dto.springapp.controller;


import com.example.dto.springapp.dtos.request.CreditDebitRequest;
import com.example.dto.springapp.dtos.request.EnquiryRequest;
import com.example.dto.springapp.dtos.request.TransferRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.dtos.response.TransactionResponse;
import com.example.dto.springapp.model.Transaction;
import com.example.dto.springapp.service.BankStatement;
import com.example.dto.springapp.service.TransactionService;
import com.example.dto.springapp.service.UserService;
import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final BankStatement bankStatement;

    public UserController(UserService userService, TransactionService transactionService, BankStatement bankStatement){
        this.userService = userService;
        this.transactionService = transactionService;
        this.bankStatement = bankStatement;
    }

    @GetMapping("/{id}")
    public BankResponse getAccount(@PathVariable long id){
        return userService.getAccount(id);
    }

    @GetMapping("/name-enquiry")
    public String getAccountName(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @GetMapping("/balance-enquiry")
    public BankResponse getBalance(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @PostMapping("/debit-account")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("/credit-account")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    @PostMapping("/transfer-account")
    public BankResponse transferAccount(@RequestBody TransferRequest request){
        return userService.TransferToAccount(request);
    }

    @GetMapping("/transactions")
    public TransactionResponse getAllTransactions(@RequestParam String accountNumber){
        return transactionService.getAllTransactions(accountNumber);
    }

    @GetMapping("/transaction")
    public TransactionResponse getTransaction(@RequestParam String transId, @RequestParam String accountNumber){
        return transactionService.getTransaction(transId, accountNumber);
    }

    @GetMapping("/statement")
    public TransactionResponse generateStatement(@RequestParam String accountNumber, @RequestParam String startDate,
                                               @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
