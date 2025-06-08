package com.example.dto.springapp.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accountNumber;
    private String accessToken;
    private String refreshToken;
}
