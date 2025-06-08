package com.example.dto.springapp.service.impl;

import com.example.dto.springapp.dtos.request.*;
import com.example.dto.springapp.dtos.response.AccountResponse;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.model.User;
import com.example.dto.springapp.repository.UserRepository;
import com.example.dto.springapp.service.EmailService;
import com.example.dto.springapp.service.TransactionService;
import com.example.dto.springapp.service.UserService;
import com.example.dto.springapp.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TransactionService transactionService;

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public BankResponse getAccount(long id) {
        if (!userRepository.existsById(id)) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIT_MESSAGE)
                    .accountResponse(null)
                    .build();
        }
        User user = userRepository.findById(id).get();

        AccountResponse res = AccountResponse.builder()
                .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                .accountNumber(user.getAccountNumber())
                .accountBalance(user.getAccountBalance())
                .build();

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_GET_CODE)
                .responseMessage(AccountUtils.ACCOUNT_GET_MESSAGE)
                .accountResponse(res)
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        if (enquiryRequest.getAccountNumber().length() != 10) {
            return "Invalid account number";
        }
        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        if (user != null) {
            return user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName();
        }
        return "User account name not found";
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        if (enquiryRequest.getAccountNumber().length() != 10) {
            return BankResponse.builder()
                    .responseCode("005")
                    .responseMessage("Invalid account number")
                    .accountResponse(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        if (user != null) {
            return BankResponse.builder()
                    .responseCode("005")
                    .responseMessage("Balance enquiry successful")
                    .accountResponse(
                            AccountResponse.builder()
                                    .accountBalance(user.getAccountBalance())
                                    .accountName(user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName())
                                    .accountNumber(user.getAccountNumber())
                                    .build()
                    )
                    .build();
        }
        return BankResponse.builder()
                .responseCode("005")
                .responseMessage("User not found")
                .accountResponse(null)
                .build();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        if(request.getAccountNumber().length() != 10){
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Invalid account number")
                    .accountResponse(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        if(user != null) {
            user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));
            User creditedUser = userRepository.save(user);
            //save transaction
            TransactionRequest transaction = TransactionRequest.builder()
                    .amount(request.getAmount())
                    .transactionType("CREDIT")
                    .accountNumber(creditedUser.getAccountNumber())
                    .build();
            transactionService.saveTransaction(transaction);

            return  BankResponse.builder()
                    .responseCode("00")
                    .responseMessage(creditedUser.getFirstName() + " credited " + request.getAmount() + " successfully")
                    .accountResponse(
                            AccountResponse.builder()
                                    .accountName(creditedUser.getFirstName() + " " + creditedUser.getOtherName() + " " + creditedUser.getLastName())
                                    .accountNumber(creditedUser.getAccountNumber())
                                    .accountBalance(creditedUser.getAccountBalance())
                                    .build()
                    )
                    .build();

        }
        return BankResponse.builder()
                .responseCode("00")
                .responseMessage("User with the account number not found")
                .accountResponse(null)
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        if(request.getAccountNumber().length() != 10){
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Invalid account number")
                    .accountResponse(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        if(user != null) {
            //check if the balance is enough
            if(user.getAccountBalance().compareTo(request.getAmount()) >= 0){
                user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));
                User debitedUser = userRepository.save(user);

                //save transaction
                TransactionRequest transaction = TransactionRequest.builder()
                        .amount(request.getAmount())
                        .transactionType("DEBIT")
                        .accountNumber(debitedUser.getAccountNumber())
                        .build();
                transactionService.saveTransaction(transaction);

                return  BankResponse.builder()
                        .responseCode("00")
                        .responseMessage(debitedUser.getFirstName() + " debited " + request.getAmount() + " successfully")
                        .accountResponse(
                                AccountResponse.builder()
                                        .accountName(debitedUser.getFirstName() + " " + debitedUser.getOtherName() + " " + debitedUser.getLastName())
                                        .accountNumber(debitedUser.getAccountNumber())
                                        .accountBalance(debitedUser.getAccountBalance())
                                        .build()
                        )
                        .build();
            }
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Insufficient balance")
                    .accountResponse(null)
                    .build();

        }
        return BankResponse.builder()
                .responseCode("00")
                .responseMessage("User with the account number not found")
                .accountResponse(null)
                .build();
    }

    @Override
    public BankResponse TransferToAccount(TransferRequest request) {
        boolean isValidRecipientAccount = request.getRecipientAccountNumber().length() == 10;
        boolean isValidSenderAccount = request.getSenderAccountNumber().length() == 10;
        if(!isValidRecipientAccount){
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Recipient Invalid account number")
                    .accountResponse(null)
                    .build();
        }
        if(!isValidSenderAccount){
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Sender Invalid account number")
                    .accountResponse(null)
                    .build();
        }
        User userSender = userRepository.findByAccountNumber(request.getSenderAccountNumber());
        User userRecipient = userRepository.findByAccountNumber(request.getRecipientAccountNumber());

        if(userSender != null && userRecipient != null) {
            if(userSender.getAccountBalance().compareTo(request.getAmount()) >= 0){
                userSender.setAccountBalance(userSender.getAccountBalance().subtract(request.getAmount()));
                User debitedUser = userRepository.save(userSender);

                userRecipient.setAccountBalance(userRecipient.getAccountBalance().add(request.getAmount()));
                User creditedUser = userRepository.save(userRecipient);

                //save transaction
                TransactionRequest transaction = TransactionRequest.builder()
                        .amount(request.getAmount())
                        .transactionType("TRANSFER")
                        .accountNumber(debitedUser.getAccountNumber())
                        .build();
                transactionService.saveTransaction(transaction);

                return  BankResponse.builder()
                        .responseCode("00")
                        .responseMessage(creditedUser.getFirstName() + " is credited " + request.getAmount() + " successfully")
                        .accountResponse(
                                AccountResponse.builder()
                                        .accountName(debitedUser.getFirstName() + " " + debitedUser.getOtherName() + " " + debitedUser.getLastName())
                                        .accountNumber(debitedUser.getAccountNumber())
                                        .accountBalance(debitedUser.getAccountBalance())
                                        .build()
                        )
                        .build();
            }
            return BankResponse.builder()
                    .responseCode("00")
                    .responseMessage("Insufficient balance")
                    .accountResponse(null)
                    .build();

        }
        return BankResponse.builder()
                .responseCode("00")
                .responseMessage("User with the account number not found")
                .accountResponse(null)
                .build();
    }
}
