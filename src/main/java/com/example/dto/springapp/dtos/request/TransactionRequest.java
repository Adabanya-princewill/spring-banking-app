package com.example.dto.springapp.dtos.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
