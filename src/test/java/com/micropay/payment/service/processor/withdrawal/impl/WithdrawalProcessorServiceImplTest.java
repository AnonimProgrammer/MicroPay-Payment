package com.micropay.payment.service.processor.withdrawal.impl;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.validator.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WithdrawalProcessorServiceImplTest {

    private WalletToCardProcessor walletToCardProcessor;
    private PaymentDataAccessService paymentDataAccessService;
    private TransactionMessageDispatcher transactionMessageDispatcher;
    private TransactionEventMapper transactionEventMapper;
    private WalletServiceAdapter walletServiceAdapter;
    private PaymentValidator paymentValidator;
    private PaymentMapper paymentMapper;

    private WithdrawalProcessorServiceImpl service;

    @BeforeEach
    void setUp() {
        walletToCardProcessor = mock(WalletToCardProcessor.class);
        paymentDataAccessService = mock(PaymentDataAccessService.class);
        transactionMessageDispatcher = mock(TransactionMessageDispatcher.class);
        transactionEventMapper = mock(TransactionEventMapper.class);
        walletServiceAdapter = mock(WalletServiceAdapter.class);
        paymentValidator = mock(PaymentValidator.class);
        paymentMapper = mock(PaymentMapper.class);

        service = new WithdrawalProcessorServiceImpl(
                walletToCardProcessor,
                paymentDataAccessService,
                transactionMessageDispatcher,
                transactionEventMapper,
                walletServiceAdapter,
                paymentValidator,
                paymentMapper
        );
        service.initializeRegistry();
    }

    @Test
    void processPaymentRequest_ShouldProcessSuccessfully() {
        PaymentRequest request = new PaymentRequest(null,
                "111", EndpointType.WALLET,
                "4111111111111111", EndpointType.CARD,
                TransactionType.WITHDRAWAL, null);

        Payment payment = new Payment();
        payment.setSource("111");
        PaymentResponse response = mock(PaymentResponse.class);
        InitiateTransactionEvent event = mock(InitiateTransactionEvent.class);

        when(paymentDataAccessService.savePayment(request)).thenReturn(payment);
        when(transactionEventMapper.mapToInitiateTransactionEvent(payment)).thenReturn(event);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = service.processPaymentRequest(request);

        assertEquals(response, result);
        verify(paymentValidator).validateTransactionType(request, TransactionType.WITHDRAWAL);
        verify(paymentValidator).validateWalletId(request.getSource());
        verify(walletServiceAdapter).reserveBalance(eq(Long.valueOf(payment.getSource())),
                any(ReservationRequest.class));
        verify(transactionMessageDispatcher).publishInitiateTransactionEvent(event);
    }

    @Test
    void processWithdrawal_ShouldCallProcessor() {
        PaymentModel paymentModel = mock(PaymentModel.class);

        when(paymentModel.getType()).thenReturn(TransactionType.WITHDRAWAL);
        when(paymentModel.getSourceType()).thenReturn(EndpointType.WALLET);
        when(paymentModel.getDestinationType()).thenReturn(EndpointType.CARD);

        service.processWithdrawal(paymentModel);

        verify(walletToCardProcessor).processPayment(paymentModel);
    }
}
