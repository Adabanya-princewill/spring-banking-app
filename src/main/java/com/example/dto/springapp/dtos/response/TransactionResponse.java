package com.example.dto.springapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String responseCode;
    @JsonProperty("message")
    private String responseMessage ;
    @JsonProperty("data")
    private List<TransResponse> transResponse;
}
