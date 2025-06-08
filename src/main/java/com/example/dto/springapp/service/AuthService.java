package com.example.dto.springapp.service;

import com.example.dto.springapp.dtos.request.LoginRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.dtos.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    BankResponse createAccount(UserRequest userRequest);

    AuthResponse login(LoginRequest request);

    AuthResponse getAccessToken(String refreshToken);
}