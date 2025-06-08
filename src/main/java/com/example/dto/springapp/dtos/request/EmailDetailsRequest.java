package com.example.dto.springapp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetailsRequest {
    private String recipient;
    private String subject;
    private String messageBody;
    private  String attachment;
}
