package com.example.dto.springapp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {
    private String recipientAccountNumber;
    private BigDecimal amount;
    private String senderAccountNumber;
}
