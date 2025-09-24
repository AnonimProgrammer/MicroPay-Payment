package com.micropay.payment.service;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.exception.PaymentNotFoundException;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.repo.PaymentRepository;
import com.micropay.payment.util.PaymentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentDataAccessService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
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

    @Transactional
    public PaymentModel updatePaymentWithTransactionId(Long id, UUID transactionId) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for ID: " + id));

        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.PROCESSING);

        paymentRepository.save(payment);
        return paymentMapper.toModel(payment);
    }

    @Transactional
    public PaymentModel updatePaymentStatus(Long id, PaymentStatus status, String failureReason) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for ID: " + id));

        payment.setStatus(status);
        if (failureReason != null) {
            payment.setFailureReason(failureReason);
        }
        updateAuditFlags(payment, status);

        return paymentMapper.toModel(payment);
    }

    // For test
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private void updateAuditFlags(Payment payment, PaymentStatus status) {
        switch (status) {
            case DEBITED -> payment.setDebitCompleted(true);
            case CREDITED -> payment.setCreditCompleted(true);
            case REFUNDED -> payment.setRefundCompleted(true);
            default -> {}
        }
    }

    public List<PaymentResponse> getPaymentHistoryByWalletId(Long walletId, LocalDateTime cursorDate, Integer limit) {
        int pageSize = (limit != null) ? limit : 20;

        List<Payment> payments = null;
        if(cursorDate == null) {
            payments = paymentRepository
                    .findPaymentsByWalletId(walletId.toString(), PageRequest.of(0, pageSize));
        } else {
            payments = paymentRepository
                    .findPaymentsByWalletIdAndCursor(walletId.toString(), cursorDate, PageRequest.of(0, pageSize));
        }
        return payments.stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}
