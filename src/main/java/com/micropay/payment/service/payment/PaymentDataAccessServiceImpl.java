package com.micropay.payment.service.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.CursorPage;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.exception.PaymentNotFoundException;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.repo.PaymentRepository;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.service.cache.CacheService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDataAccessServiceImpl implements PaymentDataAccessService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CacheService cacheService;

    private final static int DEFAULT_PAGE_SIZE = 20;

    @Override
    @Transactional
    public Payment savePayment(PaymentRequest paymentRequest) {
        log.info("Saving new payment.");
        Payment payment = paymentMapper.toEntity(paymentRequest);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public PaymentModel updatePaymentWithTransactionId(Long id, UUID transactionId) {
        log.info("Updating payment with id: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for ID: " + id));

        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.PROCESSING);

        paymentRepository.save(payment);
        return paymentMapper.toModel(payment);
    }

    @Override
    @Transactional
    public PaymentModel updatePaymentStatus(Long id, PaymentStatus status, String failureReason) {
        log.info("Updating payment with id: {}, new status: {}", id, status);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for ID: " + id));

        payment.setStatus(status);
        if (failureReason != null) payment.setFailureReason(failureReason);
        updateAuditFlags(payment, status);

        cacheService.evictAll("walletPaymentHistory");
        cacheService.evictAll("adminPayments");

        return paymentMapper.toModel(payment);
    }

    private void updateAuditFlags(Payment payment, PaymentStatus status) {
        switch (status) {
            case DEBITED -> payment.setDebitCompleted(true);
            case CREDITED -> payment.setCreditCompleted(true);
            case REFUNDED -> payment.setRefundCompleted(true);
            default -> {}
        }
    }

    @Override
    public CursorPage<PaymentResponse> getPaymentHistoryByWalletId(Long walletId, LocalDateTime cursorDate) {
        String cacheKey = walletId + "_" + (cursorDate != null ? cursorDate.toString() : "null");

        return cacheService.getOrPut("walletPaymentHistory", cacheKey,
                new TypeReference<CursorPage<PaymentResponse>>() {}, () -> {
            List<Payment> payments = paymentRepository
                    .findPaymentsByWalletIdAndCursor(walletId.toString(), cursorDate, DEFAULT_PAGE_SIZE);

            checkIfEmpty(payments);
            boolean hasNext = payments.size() > DEFAULT_PAGE_SIZE;
            LocalDateTime nextCursor = hasNext ? payments.get(DEFAULT_PAGE_SIZE).getCreatedAt() : null;

            if (hasNext) {
                payments = payments.subList(0, DEFAULT_PAGE_SIZE);
            }
            List<PaymentResponse> paymentResponses = payments.stream()
                    .map(paymentMapper::toResponse)
                    .toList();

            return new CursorPage<>(paymentResponses, nextCursor, hasNext);
        });
    }

    @Override
    public CursorPage<PaymentModel> getPayments(
            TransactionType transactionType, PaymentStatus paymentStatus,
            Long walletId, Integer pageSize, LocalDateTime cursorDate
    ) {
        final int size = (pageSize == null || pageSize > 100) ? DEFAULT_PAGE_SIZE : pageSize;

        String cacheKey = buildCacheKey(walletId, transactionType, paymentStatus, cursorDate, pageSize);

        return cacheService.getOrPut("payments", cacheKey,
                new TypeReference<CursorPage<PaymentModel>>() {}, () -> {
            List<Payment> payments = paymentRepository
                    .findPayments(walletId, transactionType, paymentStatus, cursorDate, size);

            checkIfEmpty(payments);
            boolean hasNext = payments.size() > size;
            LocalDateTime nextCursor = hasNext ? payments.get(size).getCreatedAt() : null;

            if (hasNext) {
                payments = payments.subList(0, size);
            }
            List<PaymentModel> paymentModels = payments.stream()
                    .map(paymentMapper::toModel)
                    .toList();

            return new CursorPage<>(paymentModels, nextCursor, hasNext);
        });
    }

    private String buildCacheKey(Long walletId, TransactionType transactionType,
                                 PaymentStatus paymentStatus, LocalDateTime cursorDate, Integer pageSize) {
        return (walletId != null ? walletId : "null") + "_"
                + (transactionType != null ? transactionType : "null") + "_"
                + (paymentStatus != null ? paymentStatus : "null") + "_"
                + (cursorDate != null ? cursorDate : "null") + "_"
                + pageSize;
    }

    private void checkIfEmpty(List<Payment> payments) {
        if (payments.isEmpty()) {
            throw new PaymentNotFoundException("No payments found in the system.");
        }
    }
}
