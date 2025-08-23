package com.docker.payment.dto.payment.internal.response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public ErrorResponse() {}
    public ErrorResponse(Integer status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static Integer getHttpStatus(String errorCode) {
        return statusMap.get(errorCode);
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
