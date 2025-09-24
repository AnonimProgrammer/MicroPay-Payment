package com.micropay.payment.dto.payment.external.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankApiError {

    private String errorCode;
    private String errorMessage;
    private String errorType;

    @Override
    public String toString() {
        return "BankApiError{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorType='" + errorType + '\'' +
                '}';
    }
}