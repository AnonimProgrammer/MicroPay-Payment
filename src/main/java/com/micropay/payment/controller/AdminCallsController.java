package com.micropay.payment.controller;

import com.micropay.payment.dto.payment.internal.response.CursorPage;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.service.payment.PaymentDataAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/admin/payments")
@RequiredArgsConstructor
public class AdminCallsController {

    private final PaymentDataAccessService paymentDataAccessService;

    @GetMapping
    public ResponseEntity<CursorPage<PaymentModel>> getPayments(
            @RequestParam (required = false) TransactionType transactionType,
            @RequestParam (required = false) PaymentStatus paymentStatus,
            @RequestParam (required = false) Long walletId,
            @RequestParam (required = false) Integer pageSize,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorDate
    ) {
        CursorPage<PaymentModel> paymentList = paymentDataAccessService
                .getPayments(transactionType, paymentStatus, walletId, pageSize, cursorDate);

        return ResponseEntity.ok(paymentList);
    }

}
