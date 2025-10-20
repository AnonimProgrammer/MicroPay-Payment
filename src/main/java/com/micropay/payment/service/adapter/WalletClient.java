package com.micropay.payment.service.adapter;

import com.micropay.payment.config.SystemConfiguration;
import com.micropay.payment.dto.payment.internal.request.ReservationRequest;
import com.micropay.payment.dto.payment.internal.response.ReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "wallet-service",
        url = SystemConfiguration.WALLET_SERVICE_URL,
        configuration = SystemConfiguration.class
)
public interface WalletClient {

    @PostMapping("/{id}/reserve")
    ReservationResponse reserveBalance(
            @PathVariable("id") Long walletId,
            @RequestBody ReservationRequest request
    );
}
