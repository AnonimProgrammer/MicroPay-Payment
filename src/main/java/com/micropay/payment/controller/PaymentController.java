package com.micropay.payment.controller;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.CursorPage;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import com.micropay.payment.service.processor.top_up.TopUpProcessorService;
import com.micropay.payment.service.processor.transfer.TransferProcessorService;
import com.micropay.payment.service.processor.withdrawal.WithdrawalProcessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentDataAccessService paymentDataAccessService;
    private final TopUpProcessorService topUpProcessorService;
    private final WithdrawalProcessorService withdrawalProcessorService;
    private final TransferProcessorService transferProcessorService;

    @PostMapping("/top-up")
    public ResponseEntity<PaymentResponse> top_up(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = topUpProcessorService
                .processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<PaymentResponse> withdraw(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = withdrawalProcessorService.processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<PaymentResponse> transfer(@RequestBody @Valid PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = transferProcessorService.processPaymentRequest(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/wallet/{walletId}/history")
    public ResponseEntity<CursorPage<PaymentResponse>> getPaymentHistoryByWalletId(
            @PathVariable Long walletId,
            @RequestParam(required = false) LocalDateTime cursorDate
    ) {
        CursorPage<PaymentResponse> paymentResponseList = paymentDataAccessService
                .getPaymentHistoryByWalletId(walletId, cursorDate);

        return ResponseEntity.ok(paymentResponseList);
    }

}
