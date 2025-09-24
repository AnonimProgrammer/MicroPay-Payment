package com.micropay.payment.config;

import com.micropay.payment.util.WalletErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfiguration {

    public final static String WALLET_SERVICE_URL = "http://localhost:8110/internal/wallets";

    @Bean
    public ErrorDecoder walletErrorDecoder() {
        return new WalletErrorDecoder();
    }

}
