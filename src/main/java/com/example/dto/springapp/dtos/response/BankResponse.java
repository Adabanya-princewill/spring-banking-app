package com.example.dto.springapp.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponse {
    private String responseCode;
    @JsonProperty("message")
    private String responseMessage ;
    @JsonProperty("data")
    private AccountResponse accountResponse;
}
