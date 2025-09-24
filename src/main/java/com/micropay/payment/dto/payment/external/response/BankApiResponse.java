package com.micropay.payment.dto.payment.external.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankApiResponse {

    private String status;
    private CardOperationResponse data;
    private LocalDateTime timestamp;
    private BankApiError error;

    @Override
    public String toString() {
        return "BankApiResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", error=" + error +
                '}';
    }
}


