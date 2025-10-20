package com.micropay.payment.service.processor.transfer;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.exception.InvalidPaymentRequestException;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.validator.PaymentValidator;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferProcessorServiceImplTest {

    private PaymentDataAccessService paymentDataAccessService;
    private TransactionMessageDispatcher transactionMessageDispatcher;
    private TransactionEventMapper transactionEventMapper;
    private PaymentMapper paymentMapper;
    private WalletServiceAdapter walletServiceAdapter;
    private PaymentValidator paymentValidator;

    private TransferProcessorServiceImpl service;

    @BeforeEach
    void setUp() {
        paymentDataAccessService = mock(PaymentDataAccessService.class);
        transactionMessageDispatcher = mock(TransactionMessageDispatcher.class);
        transactionEventMapper = mock(TransactionEventMapper.class);
        paymentMapper = mock(PaymentMapper.class);
        walletServiceAdapter = mock(WalletServiceAdapter.class);
        paymentValidator = mock(PaymentValidator.class);

        service = new TransferProcessorServiceImpl(
                paymentDataAccessService,
                transactionMessageDispatcher,
                transactionEventMapper,
                paymentMapper,
                walletServiceAdapter,
                paymentValidator
        );
    }

    @Test
    void processPaymentRequest_ShouldProcessSuccessfully() {
        PaymentRequest request = new PaymentRequest();
        request.setSource("1");
        request.setDestination("2");
        request.setSourceType(EndpointType.WALLET);
        request.setDestinationType(EndpointType.WALLET);
        request.setAmount(BigDecimal.valueOf(100));

        Payment payment = new Payment();
        payment.setId(123L);
        payment.setSource(request.getSource());
        payment.setAmount(request.getAmount());

        PaymentResponse response = new PaymentResponse();
        InitiateTransactionEvent event = mock(InitiateTransactionEvent.class);

        when(paymentDataAccessService.savePayment(request)).thenReturn(payment);
        when(transactionEventMapper.mapToInitiateTransactionEvent(payment)).thenReturn(event);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = service.processPaymentRequest(request);

        assertEquals(response, result);
        verify(paymentValidator).validateTransactionType(request, TransactionType.TRANSFER);
        verify(paymentValidator).validateWalletId(request.getSource());
        verify(paymentValidator).validateWalletId(request.getDestination());
        verify(walletServiceAdapter).reserveBalance(eq(Long.valueOf(payment.getSource())), any(ReservationRequest.class));
        verify(transactionMessageDispatcher).publishInitiateTransactionEvent(event);
    }

    @Test
    void processPaymentRequest_ShouldThrow_WhenEndpointsInvalid() {
        PaymentRequest request = new PaymentRequest();
        request.setSource("1");
        request.setDestination("2");
        request.setSourceType(EndpointType.WALLET);
        request.setDestinationType(EndpointType.CARD);

        assertThrows(InvalidPaymentRequestException.class,
                () -> service.processPaymentRequest(request));
    }

    @Test
    void processPaymentRequest_ShouldThrow_WhenSourceEqualsDestination() {
        PaymentRequest request = new PaymentRequest();
        request.setSource("1");
        request.setDestination("1");
        request.setSourceType(EndpointType.WALLET);
        request.setDestinationType(EndpointType.WALLET);

        assertThrows(InvalidPaymentRequestException.class,
                () -> service.processPaymentRequest(request));
    }
}
