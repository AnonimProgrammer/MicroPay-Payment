package com.micropay.payment.service.processor.refund.impl;

import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RefundProcessorServiceImplTest {

    private CardRefundProcessor cardRefundProcessor;
    private RefundProcessorServiceImpl refundProcessorService;

    @BeforeEach
    void setUp() {
        cardRefundProcessor = mock(CardRefundProcessor.class);
        refundProcessorService = new RefundProcessorServiceImpl(cardRefundProcessor);
        refundProcessorService.initializeRegistry();
    }

    @Test
    void processRefund_ShouldCallProcessorSuccessfully() {
        PaymentModel payment = new PaymentModel();
        payment.setType(TransactionType.TOP_UP);
        payment.setSourceType(EndpointType.CARD);
        payment.setDestinationType(EndpointType.WALLET);

        refundProcessorService.processRefund(payment);

        verify(cardRefundProcessor, times(1)).processPayment(payment);
    }

    @Test
    void processRefund_ShouldFail_WhenRegistryKeyNotFound() {
        PaymentModel payment = new PaymentModel();
        payment.setType(TransactionType.TOP_UP);
        payment.setSourceType(EndpointType.WALLET);
        payment.setDestinationType(EndpointType.WALLET);

        try {
            refundProcessorService.processRefund(payment);
            throw new AssertionError("Expected RuntimeException due to missing processor key");
        } catch (RuntimeException ignored) {
        }
    }
}
