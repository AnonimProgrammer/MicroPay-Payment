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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentDataAccessServiceImplTest {

    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;
    private CacheService cacheService;
    private PaymentDataAccessServiceImpl service;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentMapper = mock(PaymentMapper.class);
        cacheService = mock(CacheService.class);

        service = new PaymentDataAccessServiceImpl(paymentRepository, paymentMapper, cacheService);
    }

    @Test
    void savePayment_ShouldSavePayment() {
        PaymentRequest request = new PaymentRequest();
        Payment paymentEntity = new Payment();
        Payment savedPayment = new Payment();

        when(paymentMapper.toEntity(request)).thenReturn(paymentEntity);
        when(paymentRepository.save(paymentEntity)).thenReturn(savedPayment);

        Payment result = service.savePayment(request);

        assertEquals(savedPayment, result);
        verify(paymentRepository).save(paymentEntity);
    }

    @Test
    void updatePaymentWithTransactionId_ShouldUpdateStatusAndReturnModel() {
        Payment payment = new Payment();
        PaymentModel model = new PaymentModel();
        UUID transactionId = UUID.randomUUID();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toModel(payment)).thenReturn(model);

        PaymentModel result = service.updatePaymentWithTransactionId(1L, transactionId);

        assertEquals(model, result);
        assertEquals(transactionId, payment.getTransactionId());
        assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void updatePaymentWithTransactionId_ShouldThrowIfNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> service.updatePaymentWithTransactionId(1L, UUID.randomUUID()));
    }

    @Test
    void updatePaymentStatus_ShouldUpdatePaymentAndEvictCache() {
        Payment payment = new Payment();
        PaymentModel model = new PaymentModel();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toModel(payment)).thenReturn(model);

        PaymentModel result = service.updatePaymentStatus(1L, PaymentStatus.DEBITED, "reason");

        assertEquals(model, result);
        assertEquals(PaymentStatus.DEBITED, payment.getStatus());
        assertEquals("reason", payment.getFailureReason());
        assertTrue(payment.isDebitCompleted());

        verify(cacheService).evictAll("walletPaymentHistory");
        verify(cacheService).evictAll("adminPayments");
    }

    @Test
    void getPaymentHistoryByWalletId_ShouldReturnCachedResult() {
        Long walletId = 1L;
        LocalDateTime cursor = LocalDateTime.now();
        CursorPage<PaymentResponse> expectedPage = new CursorPage<>(List.of(), null, false);

        when(cacheService.getOrPut(eq("walletPaymentHistory"), anyString(), any(TypeReference.class), any()))
                .thenReturn(expectedPage);

        CursorPage<PaymentResponse> result = service.getPaymentHistoryByWalletId(walletId, cursor);

        assertEquals(expectedPage, result);
    }

    @Test
    void getPayments_ShouldReturnCachedResult() {
        Long walletId = 1L;
        TransactionType txType = TransactionType.TOP_UP;
        PaymentStatus status = PaymentStatus.COMPLETED;
        LocalDateTime cursor = LocalDateTime.now();
        CursorPage<PaymentModel> expectedPage = new CursorPage<>(List.of(), null, false);

        when(cacheService.getOrPut(eq("payments"), anyString(), any(TypeReference.class), any()))
                .thenReturn(expectedPage);

        CursorPage<PaymentModel> result = service.getPayments(txType, status, walletId, 10, cursor);

        assertEquals(expectedPage, result);
    }
}
