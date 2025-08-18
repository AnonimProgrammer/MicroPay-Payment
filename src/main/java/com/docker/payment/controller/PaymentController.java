package com.docker.payment.controller;

import com.docker.payment.dto.payment.internal.PaymentRequest;
import com.docker.payment.dto.payment.internal.PaymentResponse;
import com.docker.payment.service.PaymentCoordinatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentCoordinatorService paymentCoordinatorService;

    public PaymentController(PaymentCoordinatorService paymentCoordinatorService) {
        this.paymentCoordinatorService = paymentCoordinatorService;
    }

    @PostMapping("/top_up")
    public ResponseEntity<PaymentResponse> top_up(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentCoordinatorService
                .handleTopUpRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<PaymentResponse> withdrawal(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentCoordinatorService
                .handleWithdrawalRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<PaymentResponse> transfer(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentCoordinatorService
                .handleTransferRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

}
