package com.docker.payment.exception;


import com.docker.payment.dto.payment.internal.response.ErrorResponse;

public class ReservationException extends RuntimeException {
    private ErrorResponse errorResponse;

    public ReservationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
