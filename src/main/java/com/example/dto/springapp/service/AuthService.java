package com.example.dto.springapp.service;

import com.example.dto.springapp.dtos.request.EmailDetailsRequest;
import com.example.dto.springapp.dtos.request.LoginRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.AccountResponse;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.dtos.responseDtos.AuthResponse;
import com.example.dto.springapp.enums.Role;
import com.example.dto.springapp.enums.Status;
import com.example.dto.springapp.model.User;
import com.example.dto.springapp.repository.UserRepository;
import com.example.dto.springapp.utils.AccountUtils;
import com.example.dto.springapp.utils.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public BankResponse createAccount(UserRequest userRequest) {
        /*
            create user account
        * */
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                    .accountResponse(null)
                    .build();
        }

        try {
            User newUser = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .email(userRequest.getEmail())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .status(Status.ACTIVE)
                    .accountName(userRequest.getFirstName() + " " +  userRequest.getOtherName() + " " + userRequest.getLastName())
                    .accountNumber(AccountUtils.generateAccountNumber())
                    .accountBalance(BigDecimal.ZERO)
                    .username(userRequest.getEmail())
                    .role(Role.USER)
                    .build();
            User savedUser = userRepository.save(newUser);
            //send email alert
            EmailDetailsRequest emailDetails = EmailDetailsRequest.builder()
                    .recipient(savedUser.getEmail())
                    .subject("Account creation alert")
                    .messageBody("Hi, " + savedUser.getFirstName() + " Congratulations! \n" +
                            "your account has been created successfully." +
                            "\n" +
                            "\n" +
                            "See your account details below. \n" +
                            " Account name: " + savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName() + "\n" +
                            " Account number: " + savedUser.getAccountNumber()
                    )
                    .build();
            emailService.sendEmailAlert(emailDetails);
            //email sent

            AccountResponse res = AccountResponse.builder()
                    .accountName(savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName())
                    .accountNumber(savedUser.getAccountNumber())
                    .accountBalance(savedUser.getAccountBalance())
                    .build();

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_SUCCESS_MESSAGE)
                    .accountResponse(res)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(request.getEmail());

            String accessToken = jwtTokenService.generateAccessToken(user);
            String refreshToken = jwtTokenService.generateRefreshToken(user);

            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);

            return response;
        } catch (Exception e) {
            AuthResponse response = new AuthResponse();
            response.setAccessToken(null);
            response.setRefreshToken(null);

            return response;
        }
    }

    public AuthResponse getAccessToken(String refreshToken) {
        String email = jwtTokenService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email);

        if (jwtTokenService.isValidRefreshToken(refreshToken, user.getEmail())) {
            String newAccessToken = jwtTokenService.generateAccessToken(user);

            AuthResponse response = new AuthResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(refreshToken);

            return response;
        }

        AuthResponse response = new AuthResponse();
        response.setAccessToken(null);
        response.setRefreshToken(null);

        return response;
    }
}
