package com.example.dto.springapp.controller;

import com.example.dto.springapp.dtos.request.LoginRequest;
import com.example.dto.springapp.dtos.request.RefreshTokenRequest;
import com.example.dto.springapp.dtos.request.UserRequest;
import com.example.dto.springapp.dtos.response.BankResponse;
import com.example.dto.springapp.dtos.response.AuthResponse;
import com.example.dto.springapp.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return authService.createAccount(userRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/access-token")
    public AuthResponse getAccessToken(@RequestBody RefreshTokenRequest request) {
        return authService.getAccessToken(request.getRefreshToken());
    }
}
