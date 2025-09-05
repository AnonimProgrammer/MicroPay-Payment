package com.payment.exception;

public class PaymentProviderException extends RuntimeException {

    private String errorCode;

    public PaymentProviderException(String message) {
        super(message);
    }

    public PaymentProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentProviderException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PaymentProviderException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
