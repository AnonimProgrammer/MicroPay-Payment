package com.micropay.payment.exception.handler;

import com.micropay.payment.dto.payment.internal.response.ErrorResponse;
import com.micropay.payment.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentProcessorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProcessorNotFoundException(PaymentProcessorNotFoundException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(PaymentProviderException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProviderException(PaymentProviderException exception) {
        ErrorResponse body = new ErrorResponse(
                ErrorResponse.getHttpStatus(exception.getErrorCode()),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ErrorResponse.getHttpStatus(exception.getErrorCode())).body(body);
    }

    @ExceptionHandler(InvalidPaymentRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentRequestException(InvalidPaymentRequestException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception exception) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                Objects.requireNonNull(exception
                        .getBindingResult().getFieldError()).getDefaultMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request format. Check the request body.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InternalServiceCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleInternalServiceCommunicationException(InternalServiceCommunicationException exception) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> handleReservationException(ReservationException exception) {
        ErrorResponse errorResponse = exception.getErrorResponse();
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}

