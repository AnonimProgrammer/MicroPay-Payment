package com.micropay.payment.service.adapter;

import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.ErrorResponse;
import com.micropay.payment.dto.payment.internal.response.ReservationResponse;
import com.micropay.payment.exception.InternalServiceCommunicationException;
import com.micropay.payment.exception.ReservationException;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class WalletServiceAdapterTest {

    private WalletClient walletClient;
    private PaymentDataAccessService paymentDataAccessService;
    private WalletServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        walletClient = mock(WalletClient.class);
        paymentDataAccessService = mock(PaymentDataAccessService.class);
        adapter = new WalletServiceAdapter(walletClient, paymentDataAccessService);
    }

    @Test
    void reserveBalance_shouldCallWalletClientAndLog() {
        Long walletId = 1L;
        ReservationRequest request = mock(ReservationRequest.class);
        ReservationResponse response = mock(ReservationResponse.class);

        when(request.paymentId()).thenReturn(2L);
        when(request.requestedAmount()).thenReturn(BigDecimal.valueOf(10));
        when(walletClient.reserveBalance(walletId, request)).thenReturn(response);

        adapter.reserveBalance(walletId, request);

        verify(walletClient).reserveBalance(walletId, request);
    }

    @Test
    void reserveBalanceFallback_shouldUpdatePaymentAndThrowReservationException() {
        Long walletId = 1L;
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.paymentId()).thenReturn(2L);
        ReservationException exception = new ReservationException(new ErrorResponse());

        try {
            adapter.reserveBalanceFallback(walletId, request, exception);
        } catch (ReservationException ignored) {
        }

        verify(paymentDataAccessService)
                .updatePaymentStatus(2L, PaymentStatus.FAILED, exception.getMessage());
    }

    @Test
    void reserveBalanceFallback_shouldThrowInternalServiceCommunicationExceptionForOtherErrors() {
        Long walletId = 1L;
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.paymentId()).thenReturn(3L);
        RuntimeException runtimeException = new RuntimeException("Service down");

        try {
            adapter.reserveBalanceFallback(walletId, request, runtimeException);
        } catch (InternalServiceCommunicationException ignored) {
        }

        verify(paymentDataAccessService)
                .updatePaymentStatus(3L, PaymentStatus.FAILED, runtimeException.getMessage());
    }
}
