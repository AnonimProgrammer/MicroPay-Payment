package com.docker.payment.exception;

public class InternalServiceCommunicationException extends RuntimeException {

    public InternalServiceCommunicationException(String message) {
        super(message);
    }

    public InternalServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
