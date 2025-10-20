package com.micropay.payment.service.processor.withdrawal.impl;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.mapper.PaymentMapper;
import com.micropay.payment.mapper.TransactionEventMapper;
import com.micropay.payment.service.messaging.transaction.TransactionMessageDispatcher;
import com.micropay.payment.model.payment.PaymentKey;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.service.processor.withdrawal.WithdrawalProcessorService;
import com.micropay.payment.validator.PaymentValidator;
import com.micropay.payment.service.adapter.WalletServiceAdapter;
import com.micropay.payment.service.processor.PaymentProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalProcessorServiceImpl implements WithdrawalProcessorService {

    private final Map<PaymentKey, PaymentProcessor> registry = new HashMap<>();

    private final WalletToCardProcessor walletToCardProcessor;
    private final PaymentDataAccessService paymentDataAccessService;
    private final TransactionMessageDispatcher transactionMessageDispatcher;
    private final TransactionEventMapper transactionEventMapper;
    private final WalletServiceAdapter walletServiceAdapter;
    private final PaymentValidator paymentValidator;
    private final PaymentMapper paymentMapper;

    @PostConstruct
    void initializeRegistry() {
        registry.put(
                new PaymentKey(TransactionType.WITHDRAWAL, EndpointType.WALLET, EndpointType.CARD),
                walletToCardProcessor);
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        log.info("Processing payment request.");

        paymentValidator.validateTransactionType(paymentRequest, TransactionType.WITHDRAWAL);
        paymentValidator.validateWalletId(paymentRequest.getSource());

        Payment payment = paymentDataAccessService.savePayment(paymentRequest);
        reserveBalance(payment);

        InitiateTransactionEvent initiateTransactionEvent = transactionEventMapper.mapToInitiateTransactionEvent(payment);
        transactionMessageDispatcher.publishInitiateTransactionEvent(initiateTransactionEvent);

        return paymentMapper.toResponse(payment);
    }

    @Override
    public void processWithdrawal(PaymentModel payment) {
        PaymentKey key = new PaymentKey(
                payment.getType(),
                payment.getSourceType(),
                payment.getDestinationType()
        );
        PaymentProcessor paymentProcessor = getPaymentProcessor(key, registry);
        paymentProcessor.processPayment(payment);
    }

    private void reserveBalance(Payment payment) {
        walletServiceAdapter.reserveBalance(
                Long.valueOf(payment.getSource()),
                new ReservationRequest(payment.getId(), payment.getAmount()));
    }
}
