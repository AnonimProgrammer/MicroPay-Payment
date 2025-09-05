package com.payment.factory;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.model.payment.PaymentDetails;
import com.payment.model.payment.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentDtoFactory {

    public PaymentResponse createPaymentResponse(PaymentModel payment) {
        return new PaymentResponse.Builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .source(payment.getSource())
                .sourceType(payment.getSourceType())
                .destination(payment.getDestination())
                .destinationType(payment.getDestinationType())
                .type(payment.getType())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public PaymentRequest createPaymentRequest(PaymentModel payment, PaymentDetails details) {
        return new PaymentRequest(
                payment.getAmount(),
                payment.getSource(),
                payment.getSourceType(),
                payment.getDestination(),
                payment.getDestinationType(),
                payment.getType(),
                details
        );
    }
}