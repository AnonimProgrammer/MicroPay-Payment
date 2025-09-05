package com.payment.config.external;

import org.springframework.beans.factory.annotation.Value;

public class BankingConfiguration {

    @Value("${external_banking.merchant_id}")
    private static String merchantId;

    public final static String EXTERNAL_CARD_WITHDRAWAL_URL = "https://birbank.com/withdrawal";
    public final static String EXTERNAL_CARD_TOP_UP_URL = "https://birbank.com/top_up";

    public static String getMerchantId() {
        return merchantId;
    }

}
