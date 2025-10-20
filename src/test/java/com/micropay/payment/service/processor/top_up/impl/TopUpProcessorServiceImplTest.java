package com.micropay.payment.service.processor.top_up.impl;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.validator.PaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TopUpProcessorServiceImplTest {

    private CardToWalletProcessor cardToWalletProcessor;
    private PaymentDataAccessService paymentDataAccessService;
    private TransactionMessageDispatcher transactionMessageDispatcher;
    private TransactionEventMapper transactionEventMapper;
    private PaymentMapper paymentMapper;
    private PaymentValidator paymentValidator;

    private TopUpProcessorServiceImpl service;

    @BeforeEach
    void setUp() {
        cardToWalletProcessor = mock(CardToWalletProcessor.class);
        paymentDataAccessService = mock(PaymentDataAccessService.class);
        transactionMessageDispatcher = mock(TransactionMessageDispatcher.class);
        transactionEventMapper = mock(TransactionEventMapper.class);
        paymentMapper = mock(PaymentMapper.class);
        paymentValidator = mock(PaymentValidator.class);

        service = new TopUpProcessorServiceImpl(
                cardToWalletProcessor,
                paymentDataAccessService,
                transactionMessageDispatcher,
                transactionEventMapper,
                paymentMapper,
                paymentValidator
        );
        service.initializeRegistry();
    }

    @Test
    void processPaymentRequest_ShouldProcessSuccessfully() {
        PaymentRequest request = new PaymentRequest();
        request.setType(TransactionType.TOP_UP);
        request.setSourceType(EndpointType.CARD);
        request.setDestinationType(EndpointType.WALLET);
        request.setDestination("100");
        request.setAmount(java.math.BigDecimal.valueOf(500));

        Payment payment = new Payment();
        InitiateTransactionEvent event = mock(InitiateTransactionEvent.class);
        PaymentResponse response = new PaymentResponse();

        when(paymentDataAccessService.savePayment(request)).thenReturn(payment);
        when(transactionEventMapper.mapToInitiateTransactionEvent(payment)).thenReturn(event);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = service.processPaymentRequest(request);

        assertEquals(response, result);
        verify(paymentValidator).validateTransactionType(request, TransactionType.TOP_UP);
        verify(paymentValidator).validateWalletId(request.getDestination());
        verify(cardToWalletProcessor).processPayment(request);
        verify(transactionMessageDispatcher).publishInitiateTransactionEvent(event);
    }

    @Test
    void processPaymentRequest_ShouldFail_WhenInvalidProcessorKey() {
        PaymentRequest request = new PaymentRequest();
        request.setType(TransactionType.TOP_UP);
        request.setSourceType(EndpointType.WALLET);
        request.setDestinationType(EndpointType.WALLET);
        request.setDestination("100");

        assertThrows(RuntimeException.class, () -> service.processPaymentRequest(request));
    }
}
