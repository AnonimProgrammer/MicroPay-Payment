package com.micropay.payment.dto.payment.internal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;
    private String message;
    private LocalDateTime timestamp;

    private final static Map<String, Integer> statusMap = new HashMap<>(
            Map.of(
                    "INSUFFICIENT_FUNDS", 402,
                    "SERVICE_UNAVAILABLE", 503,
                    "CARD_REFUSED", 422
            )
    );

    public static Integer getHttpStatus(String errorCode) {
        return statusMap.get(errorCode);
    }

}
