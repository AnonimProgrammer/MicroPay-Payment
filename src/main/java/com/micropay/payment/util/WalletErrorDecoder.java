package com.micropay.payment.util;

import com.micropay.payment.dto.payment.internal.response.ErrorResponse;
import com.micropay.payment.dto.payment.internal.response.ReservationErrorResponse;
import com.micropay.payment.exception.ReservationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class WalletErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public WalletErrorDecoder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.status() == 400) {
                ReservationErrorResponse reservationError = objectMapper
                        .readValue(response.body().asInputStream(), ReservationErrorResponse.class);
                return new ReservationException(reservationError);
            } else if (response.status() == 404) {
                ErrorResponse errorResponse = objectMapper
                        .readValue(response.body().asInputStream(), ErrorResponse.class);
                return new ReservationException(errorResponse);
            } else {
                return new RuntimeException("Unexpected error occurred.");
            }
        } catch (IOException exception) {
            return new RuntimeException("Failed to decode error response", exception);
        }
    }
}
