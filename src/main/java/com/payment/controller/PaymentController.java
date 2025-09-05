package com.payment.controller;

import com.payment.dto.payment.internal.request.PaymentRequest;
import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.model.payment.entity.Payment;
import com.payment.service.PaymentDataAccessService;
import com.payment.service.processor.PaymentProcessorService;
import com.payment.service.processor.top_up.TopUpProcessorService;
import com.payment.service.processor.transfer.TransferProcessorService;
import com.payment.service.processor.withdrawal.WithdrawalProcessorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentDataAccessService paymentDataAccessService;
    private final PaymentProcessorService topUpProcessorService;
    private final PaymentProcessorService withdrawalProcessorService;
    private final PaymentProcessorService transferProcessorService;

    public PaymentController(
            PaymentDataAccessService paymentDataAccessService, TopUpProcessorService topUpProcessorService,
            WithdrawalProcessorService withdrawalProcessorService,
            TransferProcessorService transferProcessorService
    ) {
        this.transferProcessorService = transferProcessorService;
        this.paymentDataAccessService = paymentDataAccessService;
        this.topUpProcessorService = topUpProcessorService;
        this.withdrawalProcessorService = withdrawalProcessorService;
    }

    @PostMapping("/top_up")
    public ResponseEntity<PaymentResponse> top_up(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = topUpProcessorService
                .processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<PaymentResponse> withdrawal(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = withdrawalProcessorService
                .processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<PaymentResponse> transfer(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse =
                transferProcessorService.processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/wallet/{walletId}/history")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistoryByWalletId(
            @PathVariable Long walletId,
            @RequestParam(required = false) LocalDateTime cursorDate,
            @RequestParam(required = false) Integer limit
    ) {
        List<PaymentResponse> paymentResponseList =
                paymentDataAccessService.getPaymentHistoryByWalletId(walletId, cursorDate, limit);

        return ResponseEntity.ok(paymentResponseList);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentDataAccessService.getAllPayments();
    }

}
