package com.micropay.payment.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardDetails implements PaymentDetails {

    private String number;
    private String cvv;
    private String expirationMonth;
    private String expirationYear;

    public CardDetails(String number) {
        this.number = number;
    }

}
