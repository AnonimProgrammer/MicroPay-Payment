package com.docker.payment.service;

import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.model.payment.PaymentModel;
import com.docker.payment.model.payment.PaymentStatus;
import com.docker.payment.model.payment.entity.Payment;
import com.docker.payment.repo.PaymentRepository;
import com.docker.payment.util.PaymentMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentDataAccessService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentDataAccessService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public PaymentModel savePayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment.Builder()
                .amount(paymentRequest.getAmount())
                .status(PaymentStatus.PENDING)
                .source(paymentRequest.getSource())
                .sourceType(paymentRequest.getSourceType())
                .destination(paymentRequest.getDestination())
                .destinationType(paymentRequest.getDestinationType())
                .type(paymentRequest.getType())
                .build();
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toModel(savedPayment);
    }
}
