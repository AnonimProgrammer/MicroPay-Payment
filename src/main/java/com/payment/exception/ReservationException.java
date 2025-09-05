package com.payment.exception;


import com.payment.dto.payment.internal.response.ErrorResponse;

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
