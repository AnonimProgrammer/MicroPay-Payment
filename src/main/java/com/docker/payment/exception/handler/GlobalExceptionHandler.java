package com.docker.payment.exception.handler;

import com.docker.payment.dto.payment.ErrorResponse;
import com.docker.payment.exception.InvalidPaymentRequestException;
import com.docker.payment.exception.PaymentProcessorNotFoundException;
import com.docker.payment.exception.PaymentProviderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentProcessorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PaymentProcessorNotFoundException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(PaymentProviderException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(PaymentProviderException exception) {
        ErrorResponse body = new ErrorResponse(
                ErrorResponse.getHttpStatus(exception.getErrorCode()),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ErrorResponse.getHttpStatus(exception.getErrorCode())).body(body);
    }

    @ExceptionHandler(InvalidPaymentRequestException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(InvalidPaymentRequestException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}

