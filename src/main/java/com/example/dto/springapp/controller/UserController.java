package com.example.dto.springapp.controller;


import com.example.dto.springapp.dtos.request.CreditDebitRequest;
import com.example.dto.springapp.dtos.request.EnquiryRequest;
import com.example.dto.springapp.dtos.request.TransferRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
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
}
