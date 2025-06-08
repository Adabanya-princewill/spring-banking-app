package com.example.dto.springapp.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransResponse {
    private String transactionId;
    private String transactionType;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timeStamp;
}
