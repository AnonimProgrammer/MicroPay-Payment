package com.micropay.payment.config.external;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class BankingConfiguration {

    @Getter
    @Value("${external_banking.merchant_id}")
    private static String merchantId;

    public final static String EXTERNAL_CARD_WITHDRAWAL_URL = "https://birbank.com/withdrawal";
    public final static String EXTERNAL_CARD_TOP_UP_URL = "https://birbank.com/top_up";

}
