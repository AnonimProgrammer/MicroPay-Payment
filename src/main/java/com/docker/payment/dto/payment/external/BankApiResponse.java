package com.docker.payment.dto.payment.external;

import java.time.LocalDateTime;

public class BankApiResponse {
    private String status;
    private CardOperationResponse data;
    private LocalDateTime timestamp;
    private BankApiError error;

    public BankApiResponse() {
    }
    public BankApiResponse(String status, CardOperationResponse data, LocalDateTime timestamp, BankApiError error) {
        this.status = status;
        this.data = data;
        this.timestamp = timestamp;
        this.error = error;
    }

    public CardOperationResponse getData() {
        return data;
    }

    public void setData(CardOperationResponse data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BankApiError getError() {
        return error;
    }

    public void setError(BankApiError error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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


